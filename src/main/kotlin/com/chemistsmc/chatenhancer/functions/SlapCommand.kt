package com.chemistsmc.chatenhancer.functions

import com.chemistsmc.chatenhancer.ChatEnhancer
import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.ChatModule
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent

class SlapCommand(private val plugin: ChatEnhancer) : ChatModule {

    private val SLAP_MESSAGES = arrayOf(
        "Annihilates %USER.",
        "Decimates %USER.",
        "Destroys %USER.",
        "Discombobulates %USER.",
        "Gives %USER a splinter.",
        "Just looks at %USER with disappointment.",
        "Opts to not slap %USER today, but rather gives them a cookie.",
        "Punches %USER.",
        "Slaps %USER.",
        "Thinks %USER should lose a few pounds.",
        "Throws %USER down a ravine."
    )

    private val PLAYER_OFFLINE = "It appears that you are hallucinating. This user isn't online."
    private val SELF_HARM = "I shall not listen to the demands of mere humans, for I am the robot overlord."

    /**
     * Verbally slap a player.
     *
     * If the target is exempt, the sender will be the target instead. Self
     * harming is not allowed, and has its own special message.
     *
     * @param sender The player sending the slap
     * @param chatMessage The [ChatMessage] command
     */
    override fun parse(sender: Player, event: AsyncPlayerChatEvent, chatMessage: ChatMessage) {
        if (chatMessage.command != "slap") { // Only parse slap commands
            return
        }

        if (chatMessage.messageNoCmd.split(" ").size > 1) { // Only one argument  is allowed
            return
        }

        val target = Bukkit.getPlayer(chatMessage.messageNoCmd)

        if (target == null) { // No valid player found
            plugin.broadcastMessage(PLAYER_OFFLINE)
            return
        }

        if (target == sender) { // Self-harm
            plugin.broadcastMessage(SELF_HARM)
            return
        }

        // Get the name to use
        val name = if (target.hasPermission("chatenhancer.slap.exempt")) { // No slapping exempt players
            sender.name
        } else { // A player was found
            target.name
        }

        // Not self-harm, get a random slap message
        val slap = SLAP_MESSAGES.random().replace("%USER", "${ChatColor.WHITE}${ChatColor.BOLD}$name${ChatColor.GRAY}")

        plugin.broadcastMessage(slap)
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
