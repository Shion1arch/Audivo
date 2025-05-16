
package com.mardous.booming.extensions.media

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore.Audio.AlbumColumns
import android.provider.MediaStore.MediaColumns
import androidx.core.content.contentValuesOf
import androidx.core.net.toUri
import com.mardous.booming.R
import com.mardous.booming.extensions.hasQ
import com.mardous.booming.extensions.plurals
import com.mardous.booming.util.FileUtil
import java.io.File
import java.util.Locale

fun createAlbumArtThumbFile(): File =
    File(FileUtil.thumbsDirectory(), String.format(Locale.ROOT, "Thumb_%d", System.currentTimeMillis()))

fun ContentResolver.insertAlbumArt(albumId: Long, path: String) {
    val artworkUri = "content://media/external/audio/albumart".toUri()
    delete(ContentUris.withAppendedId(artworkUri, albumId), null, null)
    if (!hasQ()) {
        // On Android Q+, this throws an IllegalArgumentException
        // as modifying the _data column is not longer allowed
        val values = contentValuesOf(
            AlbumColumns.ALBUM_ID to albumId,
            MediaColumns.DATA to path
        )
        insert(artworkUri, values)
    }
    notifyChange(artworkUri, null)
}

fun ContentResolver.deleteAlbumArt(albumId: Long) {
    val localUri = "content://media/external/audio/albumart".toUri()
    delete(ContentUris.withAppendedId(localUri, albumId), null, null)
    notifyChange(localUri, null)
}

/**
 * iTunes uses for example 1002 for track 2 CD1 or 3011 for track 11 CD3.
 * this method converts those values to normal track numbers
 */
fun Int.trackNumber(): Int = this % 1000

fun Int.songsStr(context: Context): String = context.plurals(R.plurals.x_songs, this)

fun Int.timesStr(context: Context): String {
    return if (this <= 0) {
        context.getString(R.string.label_never)
    } else context.plurals(R.plurals.x_times, this)
}

fun Long.albumCoverUri(): Uri =
    ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), this)

fun Long.durationStr(): String {
    var minutes: Long = this / 1000 / 60
    val seconds: Long = this / 1000 % 60
    return if (minutes < 60) {
        String.format(Locale.getDefault(), "%01d:%02d", minutes, seconds)
    } else {
        val hours = minutes / 60
        minutes %= 60
        String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds)
    }
}

fun String?.sectionName(): String {
    if (this.isNullOrEmpty())
        return ""

    val pronouns = arrayOf("the ", "an ", "a ")
    var title = this.trim().lowercase()
    for (pronoun in pronouns) {
        if (title.startsWith(pronoun)) {
            title = title.substring(pronoun.length)
            break
        }
    }
    return if (title.isEmpty()) "" else title[0].toString().lowercase()
}