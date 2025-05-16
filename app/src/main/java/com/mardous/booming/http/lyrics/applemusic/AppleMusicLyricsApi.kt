
package com.mardous.booming.http.lyrics.applemusic

import com.mardous.booming.http.lyrics.LyricsApi
import com.mardous.booming.model.DownloadedLyrics
import com.mardous.booming.model.Song
import com.mardous.booming.model.toDownloadedLyrics
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.encodeURLParameter

class AppleMusicLyricsApi(private val client: HttpClient) : LyricsApi {

    override suspend fun songLyrics(
        song: Song,
        title: String,
        artist: String
    ): DownloadedLyrics? {
        return client.get("https://paxsenix.alwaysdata.net/searchAppleMusic.php") {
            url.encodedParameters.append("q", "$title $artist".encodeURLParameter())
        }.body<List<AppleSearchResponse>>().first().let {
            client.get("https://paxsenix.alwaysdata.net/getAppleMusicLyrics.php") {
                parameter("id", it.id)
            }.body<AppleLyricsResponse>().let { parseLyrics(song, it) }
        }
    }

    private fun parseLyrics(song: Song, response: AppleLyricsResponse): DownloadedLyrics? {
        if (response.content.isNullOrEmpty()) {
            return null
        }
        val syncedLyrics = StringBuilder()
        val lines = response.content
        when (response.type) {
            "Syllable" -> {
                for (line in lines) {
                    syncedLyrics.append("[${line.timestamp.toLrcTimestamp()}] ")
                    for (syllable in line.text) {
                        syncedLyrics.append(syllable.text)
                        if (!syllable.part) {
                            syncedLyrics.append(" ")
                        }
                    }
                    syncedLyrics.append("\n")
                }
            }

            "Line" -> {
                for (line in lines) {
                    syncedLyrics.append("[${line.timestamp.toLrcTimestamp()}] ${line.text[0].text}\n")
                }
            }

            else -> return null
        }
        return song.toDownloadedLyrics(syncedLyrics = syncedLyrics.toString().dropLast(1))
    }

    private fun Int.toLrcTimestamp(): String {
        val minutes = this / 60000
        val seconds = (this % 60000) / 1000
        val milliseconds = this % 1000

        val leadingZeros: Array<String> = arrayOf(
            if (minutes < 10) "0" else "",
            if (seconds < 10) "0" else "",
            if (milliseconds < 10) "00" else if (milliseconds < 100) "0" else ""
        )

        return "${leadingZeros[0]}$minutes:${leadingZeros[1]}$seconds.${leadingZeros[2]}$milliseconds"
    }
}