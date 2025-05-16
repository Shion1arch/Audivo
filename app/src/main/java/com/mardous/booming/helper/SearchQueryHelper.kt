
package com.mardous.booming.helper

import android.annotation.SuppressLint
import android.app.SearchManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import com.mardous.booming.model.Song
import com.mardous.booming.repository.RealSongRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@SuppressLint("InlinedApi")
object SearchQueryHelper : KoinComponent {

    private const val TITLE_SELECTION = "lower(${AudioColumns.TITLE}) = ?"
    private const val ALBUM_SELECTION = "lower(${AudioColumns.ALBUM}) = ?"
    private const val ARTIST_SELECTION = "lower(${AudioColumns.ARTIST}) = ?"

    private val songRepository: RealSongRepository by inject()

    @SuppressLint("DefaultLocale")
    fun getSongs(extras: Bundle): List<Song> {
        val query = extras.getString(SearchManager.QUERY, null)

        val artistName = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST, null)
        val albumName = extras.getString(MediaStore.EXTRA_MEDIA_ALBUM, null)
        val titleName = extras.getString(MediaStore.EXTRA_MEDIA_TITLE, null)

        var songs: List<Song> = ArrayList()

        if (artistName != null && albumName != null && titleName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    "$ARTIST_SELECTION AND $ALBUM_SELECTION AND $TITLE_SELECTION",
                    arrayOf(artistName.lowercase(), albumName.lowercase(), titleName.lowercase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }

        if (artistName != null && titleName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    "$ARTIST_SELECTION AND $TITLE_SELECTION",
                    arrayOf(artistName.lowercase(), titleName.lowercase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }

        if (albumName != null && titleName != null) {
            songs = songRepository.songs(
                songRepository.makeSongCursor(
                    "$ALBUM_SELECTION AND $TITLE_SELECTION",
                    arrayOf(albumName.lowercase(), titleName.lowercase())
                )
            )
        }
        if (songs.isNotEmpty()) {
            return songs
        }

        if (artistName != null) {
            songs = songRepository.songs(songRepository.makeSongCursor(ARTIST_SELECTION, arrayOf(artistName.lowercase())))
        }
        if (songs.isNotEmpty()) {
            return songs
        }

        if (albumName != null) {
            songs = songRepository.songs(songRepository.makeSongCursor(ALBUM_SELECTION, arrayOf(albumName.lowercase())))
        }
        if (songs.isNotEmpty()) {
            return songs
        }

        if (titleName != null) {
            songs = songRepository.songs(songRepository.makeSongCursor(TITLE_SELECTION, arrayOf(titleName.lowercase())))
        }
        if (songs.isNotEmpty()) {
            return songs
        }

        songs = songRepository.songs(songRepository.makeSongCursor(ARTIST_SELECTION, arrayOf(query.lowercase())))
        if (songs.isNotEmpty()) {
            return songs
        }
        songs = songRepository.songs(songRepository.makeSongCursor(ALBUM_SELECTION, arrayOf(query.lowercase())))
        if (songs.isNotEmpty()) {
            return songs
        }
        songs = songRepository.songs(songRepository.makeSongCursor(TITLE_SELECTION, arrayOf(query.lowercase())))
        return songs.ifEmpty { songRepository.songs(query) }
    }
}