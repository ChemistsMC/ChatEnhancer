package com.chemistsmc.chatenhancer.listeners

import com.chemistsmc.chatenhancer.ChatEnhancer
import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.functions.ReplacerCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(private val plugin: ChatEnhancer) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.isCancelled) { // Don't do anything for cancelled events
            return
        }

        val message = event.message

        val command = if (message.startsWith(".") && message.contains(' ')) { // Possibly a command prefix
            val prefixEnd = message.indexOf(' ')
            message.substring(1, prefixEnd)
        } else { // No command prefix, use an empty String
            ""
        }

        val messageNoCmd = if (command.isNotEmpty()) { // There is a command prefix to strip
            val prefixEnd = message.indexOf(' ') + 1 // Get rid of the space
            val toTrim = message.substring(0, prefixEnd)
            message.replace(toTrim, "").trim()
        } else { // No prefix, make it the full message
            message
        }

        val chatMessage = ChatMessage(event.player.uniqueId, command, message, messageNoCmd)

        for (module in plugin.getModuleManager().getModules()) { // Iterate through all the moduleManager
            if (module is ReplacerCommand && module.isEnabled()) { // If it's the replacer command, add the message to its cache
                module.addCachedMessage(chatMessage)
            }

            if (module.isEnabled()) { // If the module is enabled, call its Parse function
                module.parse(event.player, event, chatMessage)
            }
        }
    }
}
