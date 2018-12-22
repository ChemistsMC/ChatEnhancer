package me.ebonjaeger.chatenhancer

import java.util.regex.Pattern

object Utils {

    private val REPLACER_PATTERN = Pattern.compile("^s/[^/]+/([^/]+)\$")

    fun matchesReplacerPattern(message: String): Boolean {
        return REPLACER_PATTERN.matcher(message).matches()
    }
}
