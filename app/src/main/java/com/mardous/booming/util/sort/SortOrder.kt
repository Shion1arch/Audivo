
package com.mardous.booming.util.sort

import android.content.SharedPreferences
import androidx.core.content.edit
import com.mardous.booming.util.Preferences.requireString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SortOrder(
    private val sharedPreferences: SharedPreferences,
    private val id: String,
    private val defaultSortOrder: String,
    private val defaultDescending: Boolean
) {

    var value: String
        get() = sharedPreferences.requireString("${id}_sort_order", defaultSortOrder)
        set(value) = sharedPreferences.edit { putString("${id}_sort_order", value) }

    var isDescending: Boolean
        get() = sharedPreferences.getBoolean("${id}_descending", defaultDescending)
        set(value) = sharedPreferences.edit { putBoolean("${id}_descending", value) }

    companion object : KoinComponent {
        private val preferences: SharedPreferences by inject()
        private val sortOrderMap = hashMapOf<String, SortOrder>()

        val songSortOrder get() = sortOrder("song", SortKeys.AZ, false)

        val albumSortOrder get() = sortOrder("album", SortKeys.AZ, false)

        val albumSongSortOrder get() = sortOrder("album_song", SortKeys.TRACK_NUMBER, false)

        val similarAlbumSortOrder get() = sortOrder("similar_album", SortKeys.AZ, false)

        val artistSortOrder get() = sortOrder("artist", SortKeys.AZ, false)

        val artistAlbumSortOrder get() = sortOrder("artist_album", SortKeys.YEAR, true)

        val artistSongSortOrder get() = sortOrder("artist_song", SortKeys.AZ, false)

        val genreSortOrder get() = sortOrder("genre", SortKeys.AZ, false)

        val genreSongSortOrder get() = sortOrder("genre_song", SortKeys.AZ, false)

        val yearSortOrder get() = sortOrder("year", SortKeys.YEAR, false)

        val yearSongSortOrder get() = sortOrder("year_song", SortKeys.AZ, false)

        private fun sortOrder(key: String, defOrder: String, defDescending: Boolean) =
            sortOrderMap.getOrPut(key) { SortOrder(preferences, key, defOrder, defDescending) }
    }
}