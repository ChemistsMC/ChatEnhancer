package me.ebonjaeger.chatenhancer

import me.ebonjaeger.chatenhancer.listeners.ChatListener
import org.bukkit.plugin.java.JavaPlugin

class ChatEnhancer : JavaPlugin()
{

    override fun onEnable()
    {
        server.pluginManager.registerEvents(ChatListener(), this)
    }
}