package me.ebonjaeger.chatenhancer.functions

import me.ebonjaeger.chatenhancer.ChatEnhancer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class SlapCommand(private val plugin: ChatEnhancer) {

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
     * harming is not allowed, and has it's own special message.
     *
     * @param sender The player sending the slap
     * @param arg The target of the slap
     */
    fun parse(sender: Player, arg: String) {
        val target = Bukkit.getPlayer(arg)

        if (target == null) { // No valid player found
            Bukkit.broadcastMessage("${ChatColor.GREEN}ChatBot: ${ChatColor.GRAY}$PLAYER_OFFLINE")
            return
        }

        if (target == sender) { // Self-harm
            Bukkit.broadcastMessage("${ChatColor.GREEN}ChatBot: ${ChatColor.GRAY}$SELF_HARM")
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

        // Delay the sending else the message is sent *before* the player's chat message
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, {
            Bukkit.broadcastMessage("${ChatColor.GREEN}ChatBot: ${ChatColor.GRAY}$slap")
        }, 2L)
    }
}
