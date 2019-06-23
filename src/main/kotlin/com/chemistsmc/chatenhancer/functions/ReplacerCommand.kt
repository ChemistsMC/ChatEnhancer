package com.chemistsmc.chatenhancer.functions

import com.chemistsmc.chatenhancer.ChatEnhancer
import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.ChatModule
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * Class to handle the search and replace of previous chat messages.
 * The chat command for this function follows the pattern:
 * <pre>.r name, s/search/replace</pre>
 *
 * @property plugin The main plugin class
 */
class ReplacerCommand(private val plugin: ChatEnhancer) : ChatModule {

    private val cachedMessages = mutableListOf<ChatMessage>()
    private val cacheLimit = 15

    override fun parse(sender: Player, event: AsyncPlayerChatEvent, chatMessage: ChatMessage) {
        if (chatMessage.command != "r") { // Ignore other commands
            return
        }

        var message = if (chatMessage.messageNoCmd.startsWith("s/")) { // No name was specified, prepend the sender's
            "${sender.name},${chatMessage.messageNoCmd}"
        } else { // Remove space between name and replace text
            chatMessage.messageNoCmd.replaceFirst(", ", ",")
        }

        // Get the target's name
        val name = message.split(',', limit = 1)[0]
        val target = Bukkit.getPlayerExact(name) ?: return // Return if we can't find the target player

        for (cachedMessage in cachedMessages.reversed()) { // Look through all the cached messages from newest to oldest
            if (target.uniqueId == cachedMessage.sender && chatMessage.message != cachedMessage.message) {
                message = message.replace("s/", "")
                val messageReplacement = message.split('/')

                if (messageReplacement.size == 2) { // Make sure we have a search and replace string
                    val searchWords = messageReplacement[0]
                    val replaceWords = messageReplacement[1]

                    if (cachedMessage.message.contains(searchWords)) { // See if the cached message contains the search string
                        var newMessage = cachedMessage.message.replace(searchWords, "${ChatColor.BOLD}$replaceWords${ChatColor.GRAY}", true)
                        newMessage = newMessage.replace("  ", " ") // Remove double spaces

                        val finalMessage = if (target.name == sender.name) { // Sender is the target
                            "${sender.name} meant to say: $newMessage"
                        } else { // Someone else is the target
                            "${sender.name} thinks ${target.name} meant to say: $newMessage"
                        }

                        plugin.broadcastMessage(finalMessage)
                        break
                    }
                }
            }
        }
    }

    override fun isEnabled(): Boolean {
        return true
    }

    /**
     * Add a chat message to the cache. If adding the message puts the size of the cache
     * above the limit, remove the first element.
     *
     * @param message The [ChatMessage] to add to the cache
     */
    fun addCachedMessage(message: ChatMessage) {
        cachedMessages.add(message)

        if (cachedMessages.size > cacheLimit) { // If we're above the cache limit...
            cachedMessages.removeAt(0)
        }
    }
}
