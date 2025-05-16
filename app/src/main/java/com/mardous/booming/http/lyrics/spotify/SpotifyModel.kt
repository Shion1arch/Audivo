
package com.mardous.booming.http.lyrics.spotify

import kotlinx.serialization.Serializable

@Serializable
class SpotifyTokenResponse(
    val accessToken: String,
    val accessTokenExpirationTimestampMs: Long,
)

@Serializable
class TrackSearchResult(val tracks: Tracks) {
    @Serializable
    class Tracks(val items: List<Track>) {
        @Serializable
        class Track(val name: String, val id: String)
    }
}

@Serializable
class SyncedLinesResponse(val error: Boolean, val lines: List<Line>) {
    @Serializable
    class Line(val timeTag: String?, val words: String)
}