
package com.mardous.booming.http.lyrics

import com.mardous.booming.model.DownloadedLyrics
import com.mardous.booming.model.Song

interface LyricsApi {
    suspend fun songLyrics(song: Song, title: String, artist: String): DownloadedLyrics?
}