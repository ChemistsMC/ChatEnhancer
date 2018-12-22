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
            val mention = words.first { word -> word.startsWith('@') }.substring(1)

            val players = Bukkit.matchPlayer(mention)

            if (players.size != 1) { // More than one player matches (or none)
                return // Can't know which one was wanted, so do nothing
            }

            val target = players[0]
            val sender = event.player

            // Remove the sender and target so they don't get the original message like everyone else
            event.recipients.remove(target)
            event.recipients.remove(sender)

            // Highlight the mention to send to the sender
            val replacedMessage = message.replace("@$mention",
                "${ChatColor.YELLOW}${ChatColor.BOLD}@$mention${ChatColor.RESET}")

            // Send messages and play sound for the target
            sender.sendMessage("${sender.displayName}: $replacedMessage")
            target.sendMessage("${sender.displayName}: ${ChatColor.YELLOW}${ChatColor.BOLD}$message")
            target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1F, 1F)
        } else if (words.size == 2 && words[0].equals(".slap", true)) { // Slap command
            slapCommand.parse(event.player, words[1])
        }
    }
}
