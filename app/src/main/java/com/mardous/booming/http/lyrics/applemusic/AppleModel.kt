
package com.mardous.booming.http.lyrics.applemusic

import kotlinx.serialization.Serializable

@Serializable
class AppleSearchResponse(val id: Long, val songName: String, val artistName: String, val url: String)

@Serializable
class AppleLyricsResponse(val type: String, val content: List<AppleLyrics>?) {
    @Serializable
    class AppleLyrics(val text: List<AppleLyricsLine>, val timestamp: Int, val endtime: Int) {
        @Serializable
        class AppleLyricsLine(val text: String, val part: Boolean, val timestamp: Int?)
    }
}