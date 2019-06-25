package com.chemistsmc.chatenhancer.functions

import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.ChatModule
import com.chemistsmc.chatenhancer.config.ModuleSettings
import com.chemistsmc.chatenhancer.config.Settings
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent

class MentionPlayer(private val settings: Settings) : ChatModule {

    private val mentionsColor = ChatColor.translateAlternateColorCodes('&', settings.getProperty(ModuleSettings.MENTIONS_COLOR))

    override fun parse(sender: Player, event: AsyncPlayerChatEvent, chatMessage: ChatMessage) {
        if (chatMessage.command != "") { // Ignore actual commands
            return
        }

        if (!chatMessage.messageNoCmd.contains('@')) { // No possible mentions in this message
            return
        }

        val words = mutableListOf<String>()

        // Split the message into words
        chatMessage.messageNoCmd
            .split(' ')
            .forEach { word -> words.add(word.trim()) }

        // Get all of the mentions in the message
        val mentions = words
            .filter { it.startsWith('@') } // If the word starts with an '@' char
            .map { it.substring(1) } // Get the word minus the '@'
            .toSet()

        val targets = mentions
            .asSequence()
            .map { mention -> Bukkit.matchPlayer(mention) } // For each mention, get all matching players
            .filter { matches -> matches.size == 1 } // Only if one player is matched
            .map { matches -> matches[0] } // Add the match to the Set
            .filter { match -> match != sender } // Only add targets that aren't the sender
            .toSet()

        // Remove the sender and targets so they don't get the original message like everyone else
        event.recipients.removeAll(targets)
        event.recipients.remove(sender)

        // Highlight the mentions to send to the sender
        var replacedMessage = chatMessage.message
        mentions.forEach { mention ->
            replacedMessage = replacedMessage.replace("@$mention", "$mentionsColor@$mention${ChatColor.RESET}")
        }

        // Send messages and play sound for the target
        sender.sendMessage("${sender.displayName}: $replacedMessage")
        targets.forEach { target ->
            target.sendMessage("${sender.displayName}: $mentionsColor${chatMessage.message}")
            target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1F, 1F)
        }
    }

    override fun isEnabled(): Boolean {
        return settings.getProperty(ModuleSettings.MENTIONS_ENABLED)
    }
}
