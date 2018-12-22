package me.ebonjaeger.chatenhancer.listeners

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.entity.PlayerMock
import com.google.common.base.Charsets
import com.google.common.collect.Sets
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import me.ebonjaeger.chatenhancer.functions.SlapCommand
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Tests for [ChatListener].
 */
class ChatListenerTest {

    private val listener = ChatListener(SlapCommand())

    private val server = MockBukkit.mock()

    @Before
    fun setup() {
        server.setPlayers(5)
        server.addPlayer(
            PlayerMock(server, "Bob", UUID.nameUUIDFromBytes("OfflinePlayer:Bob".toByteArray(Charsets.UTF_8))))
        server.addPlayer(
            PlayerMock(server, "Tom", UUID.nameUUIDFromBytes("OfflinePlayer:Tom".toByteArray(Charsets.UTF_8))))
    }

    @After
    fun destroy() {
        MockBukkit.unload()
    }

    @Test
    fun mentionPlayerCorrectly() {
        // given
        val message = "hey @Tom check this out!"
        val sender = server.getPlayer("Bob") as PlayerMock
        val target = server.getPlayer("Tom") as PlayerMock
        val recipients = mutableSetOf<Player>()
        server.onlinePlayers.forEach { recipients.add(it as Player) }

        val event = AsyncPlayerChatEvent(true, sender, message, recipients)

        // when
        listener.onPlayerChat(event)

        // then
        val expectedRecipients = recipients.minus(target).minus(sender).toMutableSet()
        assertSameContents(event.recipients, expectedRecipients)

        val expectedMessageToSender = message.replace("@Tom",
            "${ChatColor.YELLOW}${ChatColor.BOLD}@Tom${ChatColor.RESET}")
        sender.assertSaid("Bob: $expectedMessageToSender")
        target.assertSaid("Bob: " + ChatColor.YELLOW + ChatColor.BOLD + message)

        // Not implemented by MockBukkit at this time
        //verify(target).playSound(target.location, Sound.BLOCK_NOTE_BLOCK_CHIME, 1F, 1F)
    }

    private fun assertSameContents(actual: MutableSet<Player>, expected: MutableSet<Player>) {
        val differences = Sets.difference(actual, expected)
        assertThat("Different number of recipients found (${differences.size})", differences.size, equalTo(0))
    }
}
