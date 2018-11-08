package me.ebonjaeger.chatenhancer.listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener : Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val message = event.message

        if (message.contains('@')) { // Message possibly contains a mention
            // Split the message into words, and add any word starting with
            // an '@' symbol to a new Set
            val words = mutableListOf<String>()
            message.split(' ')
                .filter { it.startsWith('@') }
                .forEach { words.add(it.substring(1).trim()) }

            val players = Bukkit.matchPlayer(words[0])
            if (players.size > 1) { // More than one player matches
                return // Can't know which one was wanted, so do nothing
            }

            // Message is mentioning a player
            event.recipients.removeAll(players)
            for (player in players) {
                player.sendMessage(event.player.displayName + ": " + ChatColor.YELLOW + ChatColor.BOLD + message)
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1F, 1F)
            }
        }
    }
}
