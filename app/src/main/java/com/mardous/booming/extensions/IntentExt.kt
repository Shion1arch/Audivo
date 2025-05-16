
package com.mardous.booming.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.net.toUri
import com.mardous.booming.R
import com.mardous.booming.extensions.media.displayArtistName
import com.mardous.booming.model.Song

const val MIME_TYPE_AUDIO = "audio/*"
const val MIME_TYPE_IMAGE = "image/*"
const val MIME_TYPE_PLAIN_TEXT = "text/plain"
const val MIME_TYPE_APPLICATION = "application/*"

const val EXTRA_SONG = "extra_song"
const val EXTRA_SONGS = "extra_songs"
const val EXTRA_PLAYLISTS = "extra_playlists"

fun String.openWeb(): Intent =
    Intent(Intent.ACTION_VIEW, this.toUri())
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

fun Uri.openIntent(asType: String): Intent =
    Intent(Intent.ACTION_VIEW)
        .setDataAndType(this, asType)
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

fun Context.getShareNowPlayingIntent(song: Song): Intent {
    val currentlyListening =
        getString(R.string.currently_listening_to_x_by_x, song.title, song.displayArtistName())
    return ShareCompat.IntentBuilder(this)
        .setType(MIME_TYPE_PLAIN_TEXT)
        .setText(currentlyListening)
        .createChooserIntent()
}

fun Context.getShareSongIntent(song: Song): Intent {
    return try {
        ShareCompat.IntentBuilder(this)
            .setType(MIME_TYPE_AUDIO)
            .setStream(song.mediaStoreUri)
            .setChooserTitle(R.string.action_share)
            .createChooserIntent()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        showToast("Could not share this file.")
        Intent()
    }
}

fun Context.getShareSongsIntent(songs: List<Song>): Intent {
    if (songs.size == 1) {
        return getShareSongIntent(songs.first())
    } else if (songs.isNotEmpty()) {
        val intent = ShareCompat.IntentBuilder(this)
            .setType(MIME_TYPE_AUDIO)
            .setChooserTitle(R.string.action_share)
        for (song in songs.filterNot { it == Song.emptySong }) {
            intent.addStream(song.mediaStoreUri)
        }
        return intent.createChooserIntent()
    }
    return Intent()
}

fun Intent.toChooser(chooserTitle: CharSequence? = null): Intent =
    Intent.createChooser(this, chooserTitle)