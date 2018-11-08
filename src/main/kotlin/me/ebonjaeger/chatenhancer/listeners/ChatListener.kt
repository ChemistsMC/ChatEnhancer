package me.ebonjaeger.chatenhancer.listeners

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent

class ChatListener : Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChat(event: AsyncPlayerChatEvent)
    {
        val message = event.message
        if (message.contains('@'))
        {
            // Split the message into words, and add any word starting with
            // an '@' symbol to a new Set
            val words = mutableSetOf<String>()
            message.split(' ')
                    .filter { it.startsWith('@') }
                    .forEach { words.add(it.substring(1).trim()) }

            // Iterate through all online players, and check if their name is mentioned.
            // If mentioned, add them to a Set.
            val players = mutableSetOf<Player>()
            Bukkit.getServer().onlinePlayers.forEach { player ->
                words.filter { player.name.equals(it, true) || player.name.startsWith(it, true) }
                        .forEach { players.add(player) }
            }

            if (players.isEmpty())
            {
                return
            }

            // Message contains an @-mention
            event.recipients.removeAll(players)
            for (player in players)
            {
                player.sendMessage(event.player.displayName + ": " + ChatColor.YELLOW + ChatColor.BOLD + message)
                player.playSound(player.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1F, 1F)
            }
        }
    }
}
