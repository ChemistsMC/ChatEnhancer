package com.chemistsmc.chatenhancer.config

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty
import ch.jalu.configme.properties.StringListProperty

/**
 * Class to hold settings for the included chat modules.
 */
object ModuleSettings : SettingsHolder {

    /* Mention Player Settings */
    @JvmField
    @Comment("Enable or disable the player mentions module")
    val MENTIONS_ENABLED: Property<Boolean> = newProperty("modules.mentions.enabled", true)

    @JvmField
    @Comment("Color used for mention highlights",
        "See http://ess.khhq.net/mc/ for valid codes",
        "Default: yellow, bold")
    val MENTIONS_COLOR: Property<String> = newProperty("modules.mentions.color", "&e&l")

    /* Replacer Settings */
    @JvmField
    @Comment("Enable or disable the text replacer module")
    val REPLACER_ENABLED: Property<Boolean> = newProperty("modules.replacer.enabled", true)

    @JvmField
    @Comment("Maximum number of chat messages to remember at a time")
    val CACHE_SIZE: Property<Int> = newProperty("modules.replacer.cache-size", 15)

    /* Slap Settings */
    @JvmField
    @Comment("Enable or disable the slapping module")
    val SLAPS_ENABLED: Property<Boolean> = newProperty("modules.slap.enabled", true)

    @JvmField
    @Comment("List of messages for the slap command to pick from",
        "Use the '%USER' placeholder to insert the target's name")
    val SLAP_MESSAGES: StringListProperty = StringListProperty("modules.slap.slap-messages",
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
        "Throws %USER down a ravine.")

    @JvmField
    @Comment("Message to use when a player tries to slap themselves")
    val SELF_SLAP_MESSAGE: Property<String> = newProperty("modules.slap.self-slap-message", "I shall not listen to the demands of mere humans, for I am the robot overlord.")

    /* URL Parser settings */

    @JvmField
    @Comment("Enable or disable the URL parsing module to display link titles")
    val URL_PARSER_ENABLED: Property<Boolean> = newProperty("modules.url-parser.enabled", true)

    /* Quote Module settings */

    @JvmField
    @Comment("Enable or disable the quoting module")
    val QUOTES_ENABLED: Property<Boolean> = newProperty("modules.quotes.enabled", true)

    override fun registerComments(conf: CommentsConfiguration) {
        conf.setComment("modules", "Settings for all of the included default modules")
    }
}
