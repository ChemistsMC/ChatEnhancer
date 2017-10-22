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
            val startIndex = message.indexOf('@') + 1
            var endIndex = message.length

            for (i in startIndex..message.lastIndex)
            {
                if (message[i] == ' ')
                {
                    endIndex = i
                    break
                }
            }

            val word = message.substring(startIndex, endIndex)

            var player: Player?
            player = null
            Bukkit.getServer().onlinePlayers
                    .filter { it.name == word }
                    .forEach { player = it }

            if (player == null)
            {
                return
            }

            // Message contains an @-mention
            event.recipients.remove(player)
            player?.sendMessage(event.player.displayName + ": " + ChatColor.YELLOW + ChatColor.BOLD + message)
            player?.playSound(player?.location, Sound.BLOCK_NOTE_CHIME, 1F, 1F)
        }
    }
}