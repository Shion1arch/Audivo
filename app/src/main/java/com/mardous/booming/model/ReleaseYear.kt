
package com.mardous.booming.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ReleaseYear(val year: Int, val songs: List<Song>) : Parcelable {

    val name: String
        get() = year.toString()

    val songCount: Int
        get() = songs.size

    fun safeGetFirstSong(): Song = songs.firstOrNull() ?: Song.emptySong

    companion object {
        val Empty = ReleaseYear(-1, emptyList())
    }
}