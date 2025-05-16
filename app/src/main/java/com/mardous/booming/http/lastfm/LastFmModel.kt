
package com.mardous.booming.http.lastfm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class LastFmAlbum(val album: Album?) {
    @Serializable
    class Album(val image: List<LastFmImage>, val wiki: LastFmWiki?)
}

@Serializable
class LastFmArtist(val artist: Artist?) {
    @Serializable
    class Artist(val bio: LastFmWiki?)
}

@Serializable
class LastFmImage(
    @SerialName("#text")
    val text: String?,
    val size: String?
)

@Serializable
class LastFmWiki(val content: String?)