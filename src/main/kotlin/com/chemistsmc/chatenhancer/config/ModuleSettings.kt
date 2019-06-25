package com.chemistsmc.chatenhancer.config

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newProperty

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

    /* Slap Settings */
    @JvmField
    @Comment("Enable or disable the slapping module")
    val SLAPS_ENABLED: Property<Boolean> = newProperty("modules.slaps.enabled", true)

    override fun registerComments(conf: CommentsConfiguration) {
        conf.setComment("modules", "Settings for all of the included default modules")
    }
}
