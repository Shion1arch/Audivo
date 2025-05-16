
package com.mardous.booming.repository

import android.provider.MediaStore.Audio.AudioColumns
import com.mardous.booming.model.ReleaseYear
import com.mardous.booming.model.Song
import com.mardous.booming.util.sort.SortOrder
import com.mardous.booming.util.sort.sortedSongs
import com.mardous.booming.util.sort.sortedYears

interface SpecialRepository {
    suspend fun releaseYears(): List<ReleaseYear>
    suspend fun releaseYear(year: Int): ReleaseYear
    suspend fun songs(year: Int, query: String): List<Song>
}

class RealSpecialRepository(private val songRepository: RealSongRepository) : SpecialRepository {

    override suspend fun releaseYears(): List<ReleaseYear> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor("${AudioColumns.YEAR} > 0", null)
        )
        val grouped = songs.groupBy { it.year }
        return grouped.map { ReleaseYear(it.key, it.value) }.sortedYears(SortOrder.yearSortOrder)
    }

    override suspend fun releaseYear(year: Int): ReleaseYear {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(
                selection = "${AudioColumns.YEAR}=?",
                selectionValues = arrayOf(year.toString())
            )
        )
        return ReleaseYear(year, songs.sortedSongs(SortOrder.yearSongSortOrder))
    }

    override suspend fun songs(year: Int, query: String): List<Song> {
        return songRepository.songs(
            songRepository.makeSongCursor(
                selection = "${AudioColumns.YEAR}=? AND ${AudioColumns.TITLE} LIKE ?",
                selectionValues = arrayOf(year.toString(), "%$query%")
            )
        )
    }
}