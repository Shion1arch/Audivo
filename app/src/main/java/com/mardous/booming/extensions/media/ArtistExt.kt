
package com.mardous.booming.extensions.media

import android.content.Context
import com.mardous.booming.R
import com.mardous.booming.appContext
import com.mardous.booming.extensions.plurals
import com.mardous.booming.extensions.utilities.buildInfoString
import com.mardous.booming.model.Artist
import java.util.regex.Pattern

fun String.displayArtistName(): String =
    when {
        isArtistNameUnknown() -> appContext().getString(R.string.unknown_artist)
        isVariousArtists() -> appContext().getString(R.string.various_artists)
        else -> this
    }

fun Artist.isNameUnknown() = name.isArtistNameUnknown()

fun Artist.artistInfo(context: Context) =
    buildInfoString(albumCountStr(context), songCountStr(context))

fun Artist.albumCountStr(context: Context) = context.plurals(R.plurals.x_albums, albumCount)

fun Artist.songCountStr(context: Context): String = songCount.songsStr(context)

fun Artist.displayName() = name.displayArtistName()

internal fun String?.isVariousArtists(): Boolean {
    if (isNullOrBlank())
        return false
    if (this == Artist.VARIOUS_ARTISTS_DISPLAY_NAME)
        return true
    return false
}

internal fun String?.isArtistNameUnknown(): Boolean =
    if (isNullOrBlank()) false
    else trim().let { it.equals("unknown", true) || it.equals("<unknown>", true) }

/**
 * A pattern that matches any artist name containing some sequences like "feat.", "ft.", "featuring"
 * and/or other special characters like "/" and ";".
 */
private val artistNamePattern = Pattern.compile("(.*)([(?\\s](feat(uring)?|ft)\\.? |\\s?[/;]\\s?)(.*)")

fun String.toAlbumArtistName(): String {
    val matcher = artistNamePattern.matcher(lowercase())
    if (matcher.matches()) {
        val goIndex = matcher.start(2)
        return this.substring(0, goIndex).trim()
    }
    return this
}