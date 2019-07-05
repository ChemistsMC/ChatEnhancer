package com.chemistsmc.chatenhancer.functions

import com.chemistsmc.chatenhancer.ChatEnhancer
import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.ChatModule
import com.chemistsmc.chatenhancer.config.ModuleSettings
import com.chemistsmc.chatenhancer.config.Settings
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.util.UUID

class QuoteModule(private val plugin: ChatEnhancer,
                  private val settings: Settings) : ChatModule {

    private val gson = GsonBuilder().setPrettyPrinting().create()
    private val quotesFile = File(plugin.dataFolder, "quotes.json")

    init {
        if (!Files.exists(quotesFile.toPath())) { // Make sure the quotes file exists
            Files.createFile(quotesFile.toPath())

            // Create and write an empty JSON object
            val jsonRoot = JsonObject()
            val jsonArray = JsonArray()
            jsonRoot.add("quotes", jsonArray)

            FileWriter(quotesFile).use { writer ->
                writer.write(gson.toJson(jsonRoot))
            }
        }
    }

    override fun parse(sender: Player, event: AsyncPlayerChatEvent, chatMessage: ChatMessage) {
        if (chatMessage.command != "quote") { // Ignore other commands
            return
        }

        if (chatMessage.messageNoCmd.startsWith("add ")) { // User wants to add a quote
            if (chatMessage.messageNoCmd.split(' ').size > 2) { // We have a sender and a message
                val preSplit = chatMessage.messageNoCmd.replaceFirst("add ", "") // Remove 'add' keyword
                val parts = preSplit.split(' ', limit = 2) // Split out the first word from the rest
                val target = Bukkit.getOfflinePlayer(parts[0])

                if (!target.hasPlayedBefore()) { // No matching player was found currently online
                    plugin.broadcastMessage("I'm afraid I don't know who '${parts[0]}' is.")
                    return
                }

                val quote = Quote(target.uniqueId, parts[1])
                addQuote(quote)
                plugin.broadcastMessage("${ChatColor.GREEN}Quote added successfully!")
            }
        }
    }

    override fun isEnabled(): Boolean {
        return settings.getProperty(ModuleSettings.QUOTES_ENABLED)
    }

    private fun addQuote(quote: Quote) {
        val root = JsonReader(FileReader(quotesFile)).use { reader ->
            val parser = JsonParser()
            parser.parse(reader).asJsonObject
        }

        val quotes = root["quotes"].asJsonArray
        val jsonQuote = gson.toJson(quote)
        quotes.add(jsonQuote)
        root.add("quotes", quotes)

        FileWriter(quotesFile).use { writer ->
            writer.write(gson.toJson(root))
        }
    }

    /**
     * Class to hold information about a quoted message.
     *
     * @property sender The player being quoted
     * @property message The message the player sent
     */
    inner class Quote(val sender: UUID,
                      val message: String)
}
