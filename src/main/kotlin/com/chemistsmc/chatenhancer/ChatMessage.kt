package com.chemistsmc.chatenhancer

import java.util.*

/**
 * Class to hold information about a chat message or command.
 *
 * @property sender The [UUID] of the player that sent the message
 * @property command A command that the plugin supports, or an empty String
 * @property message The full chat message
 * @property messageNoCmd The chat message without the command prefix, if it exists
 */
data class ChatMessage(val sender: UUID,
                       val command: String,
                       val message: String,
                       val messageNoCmd: String)
