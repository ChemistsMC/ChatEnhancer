package com.chemistsmc.chatenhancer.config

import ch.jalu.configme.SettingsManagerImpl
import ch.jalu.configme.configurationdata.ConfigurationData
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder
import ch.jalu.configme.migration.MigrationService
import ch.jalu.configme.migration.PlainMigrationService
import ch.jalu.configme.resource.YamlFileResource
import java.io.File

class Settings private constructor(resource: YamlFileResource,
                                   configurationData: ConfigurationData,
                                   migrator: MigrationService) : SettingsManagerImpl(resource, configurationData, migrator) {

    companion object {
        private val propertyHolders = arrayOf(
            PluginSettings::class.java,
            ModuleSettings::class.java
        )

        /**
         * Creates a [Settings] instance, using the given file as config file.
         *
         * @param file The config file to load
         * @return Settings instance for the file
         */
        fun create(file: File): Settings {
            val fileResource = YamlFileResource(file)
            val configurationData = ConfigurationDataBuilder.createConfiguration(*propertyHolders)
            val migrator = PlainMigrationService()

            return Settings(fileResource, configurationData, migrator)
        }
    }
}
