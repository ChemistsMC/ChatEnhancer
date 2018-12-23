package me.ebonjaeger.chatenhancer.listeners

import me.ebonjaeger.chatenhancer.functions.SlapCommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener(private val slapCommand: SlapCommand) : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        if (event.isCancelled) { // Don't do anything for cancelled events
            return
        }

        val message = event.message
        val words = mutableListOf<String>()

        // Split the message into words
        message.split(' ')
            .forEach { word -> words.add(word.trim()) }

        if (message.contains('@')) { // Message possibly contains a mention
            // Get all of the mentions in the message
            val mentions = words
                .filter { it.startsWith('@') } // If the word starts with an '@' char
                .map { it.substring(1) } // Get the word minus the '@'
                .toSet()

            val sender = event.player
            val targets = mentions
                .map { mention -> Bukkit.matchPlayer(mention) } // For each mention, get all matching players
                .filter { matches -> matches.size == 1 } // Only if one player is matched
                .map { matches -> matches[0] } // Add the match to the Set
                .toSet()

            // Remove the sender and targets so they don't get the original message like everyone else
            event.recipients.removeAll(targets)
            event.recipients.remove(sender)

            // Highlight the mentions to send to the sender
            var replacedMessage = message
            mentions.forEach { mention ->
                replacedMessage = replacedMessage.replace("@$mention", "${ChatColor.YELLOW}${ChatColor.BOLD}@$mention${ChatColor.RESET}")
            }

            // Send messages and play sound for the target
            sender.sendMessage("${sender.displayName}: $replacedMessage")
            targets.forEach { target ->
                target.sendMessage("${sender.displayName}: ${ChatColor.YELLOW}${ChatColor.BOLD}$message")
                target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1F, 1F)
            }
        } else if (words.size == 2 && words[0].equals(".slap", true)) { // Slap command
            slapCommand.parse(event.player, words[1])
        }
    }
}
