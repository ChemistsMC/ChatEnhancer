package me.ebonjaeger.chatenhancer

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

/**
 * Tests for [Utils].
 */
class UtilsTest {

    @Test
    fun correctlyMatchesString() {
        // given
        val message = "s/blah blah blah/more blah"

        // when
        val result = Utils.matchesReplacerPattern(message)

        // then
        assertThat(result, equalTo(true))
    }

    @Test
    fun correctlyDoesntMatchStringWithSlashes() {
        // given
        val message = "random message/with slashes/hodor"

        // when
        val result = Utils.matchesReplacerPattern(message)

        // then
        assertThat(result, equalTo(false))
    }

    @Test
    fun correctlyDoesntMatchStringWithExtraSlashes() {
        // given
        val message = "s/random message/with slashes/more"

        // when
        val result = Utils.matchesReplacerPattern(message)

        // then
        assertThat(result, equalTo(false))
    }

    @Test
    fun correctlyDoesntMatchStringWithTooFewSlashes() {
        // given
        val message = "s/random message"

        // when
        val result = Utils.matchesReplacerPattern(message)

        // then
        assertThat(result, equalTo(false))
    }
}
