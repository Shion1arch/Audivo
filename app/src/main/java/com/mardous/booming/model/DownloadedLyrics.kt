
package com.mardous.booming.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DownloadedLyrics(
    val id: Int,
    @SerialName("trackName")
    val title: String,
    @SerialName("artistName")
    val artist: String,
    @SerialName("albumName")
    val album: String,
    val duration: Double,
    val plainLyrics: String?,
    val syncedLyrics: String?
) {

    val isSynced: Boolean
        get() = !syncedLyrics.isNullOrEmpty()

    val hasMultiOptions: Boolean
        get() = !plainLyrics.isNullOrEmpty() && !syncedLyrics.isNullOrEmpty()
}

fun Song.toDownloadedLyrics(plainLyrics: String? = null, syncedLyrics: String? = null) =
    DownloadedLyrics(id.toInt(), title, artistName, albumName, (duration / 1000).toDouble(), plainLyrics, syncedLyrics)