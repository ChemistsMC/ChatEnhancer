package com.chemistsmc.chatenhancer

import com.chemistsmc.chatenhancer.config.PluginSettings
import com.chemistsmc.chatenhancer.config.Settings
import com.chemistsmc.chatenhancer.functions.MentionPlayer
import com.chemistsmc.chatenhancer.functions.QuoteModule
import com.chemistsmc.chatenhancer.functions.ReplacerCommand
import com.chemistsmc.chatenhancer.functions.SlapCommand
import com.chemistsmc.chatenhancer.functions.URLParser
import com.chemistsmc.chatenhancer.listeners.ChatListener
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.nio.file.Files

class ChatEnhancer : JavaPlugin() {

    private val moduleManager = ModuleManager(this)
    private var prefix = ""

    override fun onEnable() {
        // Create config file it it hasn't yet been saved
        if (!Files.exists(File(dataFolder, "config.yml").toPath())) {
            saveResource("config.yml", false)
        }

        // Initialize our settings
        val settings = Settings.create(File(dataFolder, "config.yml"))

        prefix = ChatColor.translateAlternateColorCodes('&', settings.getProperty(PluginSettings.CHATBOT_PREFIX))

        // Create our default modules
        moduleManager.loadModule(MentionPlayer(settings))
        moduleManager.loadModule(QuoteModule(this, settings))
        moduleManager.loadModule(ReplacerCommand(this, settings))
        moduleManager.loadModule(SlapCommand(this, settings))
        moduleManager.loadModule(URLParser(this, settings))

        // Register our chat listener
        server.pluginManager.registerEvents(ChatListener(this), this)
    }

    /**
     * Get the plugin's [ModuleManager]. This is exposed in case
     * other plugins want to add their own [ChatModule]'s.
     *
     * @return The module manager
     */
    fun getModuleManager(): ModuleManager = moduleManager

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
