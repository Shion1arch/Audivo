
package com.mardous.booming.extensions.media

import android.content.Context
import com.mardous.booming.extensions.utilities.buildInfoString
import com.mardous.booming.model.Album

fun Album.isArtistNameUnknown() = albumArtistName().isArtistNameUnknown()

fun Album.albumArtistName() = if (albumArtistName.isNullOrBlank()) artistName else albumArtistName!!

fun Album.displayArtistName() = albumArtistName().displayArtistName()

fun Album.albumInfo(): String = when {
    year > 0 -> buildInfoString(displayArtistName(), year.toString())
    else -> displayArtistName()
}

fun Album.songCountStr(context: Context) = songCount.songsStr(context)