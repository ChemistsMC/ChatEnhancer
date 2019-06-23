package com.chemistsmc.chatenhancer

import com.chemistsmc.chatenhancer.functions.MentionPlayer
import com.chemistsmc.chatenhancer.functions.ReplacerCommand
import com.chemistsmc.chatenhancer.functions.SlapCommand
import com.chemistsmc.chatenhancer.listeners.ChatListener
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin

class ChatEnhancer : JavaPlugin() {

    private val chatModules = mutableSetOf<ChatModule>()
    private val prefix = "${ChatColor.GREEN}ChatBot: ${ChatColor.GRAY}"

    override fun onEnable() {
        // Create our default modules
        chatModules.add(MentionPlayer())
        chatModules.add(ReplacerCommand(this))
        chatModules.add(SlapCommand(this))

        // Register our chat listener
        server.pluginManager.registerEvents(ChatListener(chatModules), this)
    }

    /**
     * Broadcast a message to everyone on the server.
     * Sending is delayed 2 ticks to prevent messages being sent before
     * the actual chat message is sent to the other players.
     *
     * @param message The message to send
     */
    fun broadcastMessage(message: String) {
        server.scheduler.scheduleSyncDelayedTask(this, {
            server.broadcastMessage("$prefix $message")
        }, 2L)
    }
}
