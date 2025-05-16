
package com.mardous.booming.mvvm

import com.mardous.booming.lyrics.LrcLyrics

class LyricsResult(
    val id: Long,
    val data: String? = null,
    val lrcData: LrcLyrics = LrcLyrics(),
    val fromLocalFile: Boolean = false,
    val loading: Boolean = false,
) {
    val hasData: Boolean get() = !data.isNullOrEmpty()
    val isSynced: Boolean get() = lrcData.hasLines
    val isEmpty: Boolean get() = !hasData && !isSynced
}