package com.chemistsmc.chatenhancer.functions

import com.chemistsmc.chatenhancer.ChatEnhancer
import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.ChatModule
import com.chemistsmc.chatenhancer.config.ModuleSettings
import com.chemistsmc.chatenhancer.config.Settings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.*

/**
 * Class to handle the search and replace of previous chat messages.
 * The chat command for this function follows the pattern:
 * <pre>.r name s/search/replace</pre>
 *
 * @property plugin The main plugin class
 */
class ReplacerCommand(private val plugin: ChatEnhancer,
                      private val settings: Settings) : ChatModule {

    private val PLAYER_OFFLINE = "It appears that you are hallucinating. This user isn't online."

    private val cachedMessages = Collections.synchronizedList(mutableListOf<ChatMessage>())
    private val cacheLimit = 15

    override fun parse(sender: Player, event: AsyncPlayerChatEvent, chatMessage: ChatMessage) {
        if (chatMessage.command != "r") { // Ignore other commands
            return
        }

        // Get the target
        val target = getTarget(sender, chatMessage.messageNoCmd)

        if (target == null) { // No valid player found
            plugin.broadcastMessage(PLAYER_OFFLINE)
            return
        }

        val replacer = chatMessage.messageNoCmd.substringAfter("s/")
        val messageReplacement = replacer.split('/')

        if (messageReplacement.size != 2) {// Make sure we have a search and replace string
            return
        }

        val searchWords = messageReplacement[0]
        val replaceWords = messageReplacement[1]

        synchronized(cachedMessages) {
            // Synchronize on access since writes and reads to this are async
            for (cachedMessage in cachedMessages.reversed()) { // Look through all the cached messages from newest to oldest
                if (target.uniqueId == cachedMessage.sender && chatMessage.message != cachedMessage.message) {
                    if (cachedMessage.message.contains(searchWords)) { // See if the cached message contains the search string
                        var newMessage = cachedMessage.message.replace(searchWords, "${ChatColor.BOLD}$replaceWords${ChatColor.GRAY}", true)
                        newMessage = newMessage.replace("  ", " ") // Remove double spaces

                        val finalMessage = if (target.name == sender.name) { // Sender is the target
                            "${sender.name} meant to say: $newMessage"
                        } else { // Someone else is the target
                            "${sender.name} thinks ${target.name} meant to say: $newMessage"
                        }

                        plugin.broadcastMessage(finalMessage)
                    }

                    break // Stop after finding the user's most recent message
                }
            }
        }
    }

    override fun isEnabled(): Boolean {
        return settings.getProperty(ModuleSettings.REPLACER_ENABLED)
    }

    /**
     * Add a chat message to the cache. If adding the message puts the size of the cache
     * above the limit, the first element will be removed.
     *
     * @param message The [ChatMessage] to add to the cache
     */
    fun addCachedMessage(message: ChatMessage) {
        cachedMessages.add(message)

        if (cachedMessages.size > cacheLimit) { // If we're above the cache limit...
            cachedMessages.removeAt(0)
        }
    }

    /**
     * Get the target player for the replacement. Will return
     * null if no matching player is found. If there is no name
     * specified, the sender will be returned.
     *
     * @return The target of the command
     */
    private fun getTarget(sender: Player, message: String): Player? {
        if (message.startsWith("s/")) {
            return sender
        }

        val name = message.substringBefore(' ')
        return Bukkit.getPlayer(name)
    }
}
