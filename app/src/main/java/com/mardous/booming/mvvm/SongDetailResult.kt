
package com.mardous.booming.mvvm

data class SongDetailResult(
    val playCount: String? = null,
    val skipCount: String? = null,
    val lastPlayedDate: String? = null,
    val filePath: String? = null,
    val fileSize: String? = null,
    val trackLength: String? = null,
    val dateModified: String? = null,
    val audioHeader: String? = null,
    val title: String? = null,
    val album: String? = null,
    val artist: String? = null,
    val albumArtist: String? = null,
    val albumYear: String? = null,
    val trackNumber: String? = null,
    val discNumber: String? = null,
    val composer: String? = null,
    val conductor: String? = null,
    val publisher: String? = null,
    val genre: String? = null,
    val replayGain: String? = null,
    val comment: String? = null
) {
    companion object {
        val Empty = SongDetailResult()
    }
}