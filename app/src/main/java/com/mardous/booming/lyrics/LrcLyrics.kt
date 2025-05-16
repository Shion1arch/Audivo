
package com.mardous.booming.lyrics


data class LrcLyrics @JvmOverloads constructor(
    val offset: Long = 0,
    val lines: MutableList<LrcEntry> = ArrayList()
) {

    val hasLines: Boolean
        get() = lines.any { it.text.isNotBlank() }

    fun getValidLines() = lines.filter { it.text.isNotBlank() }

    fun getText(): String {
        return lines.joinToString("\n") { it.getFormattedText() }
    }

    fun clear() {
        lines.clear()
    }
}