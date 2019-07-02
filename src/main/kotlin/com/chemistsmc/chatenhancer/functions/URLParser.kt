package com.chemistsmc.chatenhancer.functions

import com.chemistsmc.chatenhancer.ChatEnhancer
import com.chemistsmc.chatenhancer.ChatMessage
import com.chemistsmc.chatenhancer.ChatModule
import com.chemistsmc.chatenhancer.config.ModuleSettings
import com.chemistsmc.chatenhancer.config.Settings
import com.chemistsmc.chatenhancer.util.Patterns
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URL

/**
 * Class to parse URL links in the chat. The [parse] function
 * will broadcast the page's title in the chat. If the link is
 * to a Reddit post, additional information will be shown like
 * the post's total score and the percentage of upvotes to downvotes.
 *
 * @property plugin The [ChatEnhancer] plugin instance
 * @property settings The [Settings] instance
 */
class URLParser(private val plugin: ChatEnhancer,
                private val settings: Settings) : ChatModule {

    override fun parse(sender: Player, event: AsyncPlayerChatEvent, chatMessage: ChatMessage) {
        val urls = mutableListOf<String>()
        val splitMessage = chatMessage.message.split(' ')

        for (part in splitMessage) {
            if (Patterns.WEB_URL.matcher(part).matches()) { // If the message part is a URL, add it to the list
                var url = part
                if (!url.startsWith("http://") && !url.startsWith("https://")) { // Prepend protocol if it isn't given
                    url = "https://$url"
                }

                if (url.contains("reddit.com") && !url.contains("old.reddit.com")) { // Old Reddit is more parse-friendly
                    url = url.replace("reddit.com", "old.reddit.com")
                    url = url.replace("www.", "") // Remove 'www' if present after conversion
                }

                urls.add(url)
            }
        }

        if (urls.isEmpty()) { // Do nothing if there are no URLs
            return
        }

        plugin.server.scheduler.runTaskAsynchronously(plugin) { _ ->
            // Don't tie up the chat thread with web lookups
            val links = mutableListOf<ChatLink>()
            for (url in urls) {
                val response = try {
                    khttp.get(url, headers = mapOf("User-Agent" to "MC ChatBot 2.0.0"))
                } catch (ex: IOException) {
                    null
                } // If there was an exception while getting the page, move on to the next one

                if (response != null && response.statusCode == 200) { // Successfully got a page
                    val contentType = response.headers["Content-Type"]
                    if (contentType != null && contentType.contains("text/html")) {
                        val document = Jsoup.parse(response.text) // Parse the HTML
                        val host = response.connection.url.host
                        val title = document.title()

                        var isReddit = false
                        var redditVotes: RedditVotes? = null

                        if (host == "old.reddit.com") { // Reddit link
                            isReddit = true
                            redditVotes = RedditVotes(
                                document.select(".unvoted > .likes").text().toIntOrNull(),
                                document.select(".unvoted > .dislikes").text().toIntOrNull(),
                                document.select(".unvoted > .unvoted").text().toIntOrNull()
                            )
                        }

                        links.add(ChatLink(response.connection.url, title, isReddit, redditVotes))
                    }
                }
            }

            for (link in links) {
                when {
                    link.isReddit -> parseReddit(link)
                    else -> plugin.broadcastMessage("[ ${link.title} ]")
                }
            }
        }
    }

    override fun isEnabled(): Boolean {
        return settings.getProperty(ModuleSettings.URL_PARSER_ENABLED)
    }

    /**
     * Function to specially handle information about a Reddit link.
     * If the link is to a post, it will print the post's score and upvoted percent.
     *
     * @param link The [ChatLink] to pull information from
     */
    private fun parseReddit(link: ChatLink) {
        val title: String
        if (link.votes?.upvotes != null && link.votes.downvotes != null && link.votes.score != null) { // If we have voting information, format it
            var percentage = ((link.votes.downvotes.toDouble() / link.votes.upvotes.toDouble()) * 100).toInt()
            if (percentage == 0) { // 100% upvoted
                percentage = 100
            }

            title = "[ ${link.title} | Score: ${link.votes.score} | Upvoted: $percentage% ]"
        } else {
            title = "[ ${link.title} ]"
        }

        plugin.broadcastMessage(title)
    }

    /**
     * Class for holding information about a link in the chat.
     *
     * @param url The [URL] the link points to
     * @param title The title of the web page
     * @param isReddit True if the link points to Reddit
     * @param votes If the link is to a Reddit post, it will have the post's score information
     */
    inner class ChatLink(val url: URL,
                         val title: String,
                         val isReddit: Boolean,
                         val votes: RedditVotes?
    )

    /**
     * Class for holding Reddit post score information.
     * Values will be null if they are not found in the page.
     *
     * @param upvotes The number of upvotes the post has
     * @param downvotes The number of downvotes the post has
     * @param score The final score of the post
     */
    inner class RedditVotes(val upvotes: Int?,
                            val downvotes: Int?,
                            val score: Int?)
}
