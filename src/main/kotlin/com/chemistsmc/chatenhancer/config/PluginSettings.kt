package com.chemistsmc.chatenhancer.config

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty

/**
 * Class to hold general plugin settings.
 */
object PluginSettings : SettingsHolder {

    @JvmField
    @Comment("Text to place in front of messages sent from the plugin",
        "Essentially, the this is the display name for the bot")
    val CHATBOT_PREFIX: Property<String> = newProperty("chatbot-prefix", "&2ChatBot:&7")
}
