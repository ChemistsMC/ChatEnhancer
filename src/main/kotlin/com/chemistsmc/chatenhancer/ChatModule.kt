package com.chemistsmc.chatenhancer

import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent

/**
 * Base interface for a chat function.
 */
interface ChatModule {

    /**
     * Parse a chat message and perform the module's function.
     *
     * @param sender The [Player] that sent the chat message
     * @param event The chat event
     * @param chatMessage The [ChatMessage] for the message
     */
    fun parse(sender: Player, event: AsyncPlayerChatEvent, chatMessage: ChatMessage)

    /**
     * See if a particular function is enabled.
     *
     * @return True if the function is enabled
     */
    fun isEnabled(): Boolean
}
