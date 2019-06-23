package com.chemistsmc.chatenhancer.listeners

import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.ChatModule
import com.chemistsmc.chatenhancer.functions.ReplacerCommand
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(private val modules: Set<ChatModule>) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.isCancelled) { // Don't do anything for cancelled events
            return
        }

        val command = if (event.message.startsWith(".")) { // Get the command prefix
            val prefixEnd = event.message.indexOf(' ')
            event.message.substring(1, prefixEnd)
        } else { // No command prefix, use an empty String
            ""
        }

        val messageNoCmd = if (command.isNotEmpty()) event.message.replace(command, "").trim() else event.message

        val chatMessage = ChatMessage(event.player.uniqueId, command, event.message, messageNoCmd)

        for (module in modules) { // Iterate through all the modules
            if (module is ReplacerCommand && module.isEnabled()) { // If it's the replacer command, add the message to its cache
                module.addCachedMessage(chatMessage)
            }

            if (module.isEnabled()) { // If the module is enabled, call its Parse function
                module.parse(event.player, event, chatMessage)
            }
        }
    }
}
