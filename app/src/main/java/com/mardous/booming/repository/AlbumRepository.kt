
package com.mardous.booming.repository

import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import com.mardous.booming.model.Album
import com.mardous.booming.model.Song
import com.mardous.booming.util.Preferences
import com.mardous.booming.util.sort.SortOrder
import com.mardous.booming.util.sort.sortedAlbums
import com.mardous.booming.util.sort.sortedSongs

interface AlbumRepository {
    fun album(albumId: Long): Album
    fun albums(): List<Album>
    fun albums(query: String): List<Album>
    fun similarAlbums(album: Album): List<Album>
}

class RealAlbumRepository(private val songRepository: RealSongRepository) : AlbumRepository {

    override fun album(albumId: Long): Album {
        val cursor = songRepository.makeSongCursor(
            AudioColumns.ALBUM_ID + "=?",
            arrayOf(albumId.toString()),
            DEFAULT_SORT_ORDER
        )
        val songs = songRepository.songs(cursor)
        val album = Album(albumId, songs)
        return sortAlbumSongs(album)
    }

    override fun albums(query: String): List<Album> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(AudioColumns.ALBUM + " LIKE ?", arrayOf("%$query%"), DEFAULT_SORT_ORDER)
        )
        return splitIntoAlbums(songs)
    }

    override fun albums(): List<Album> {
        val songs = songRepository.songs(
            songRepository.makeSongCursor(null, null, DEFAULT_SORT_ORDER)
        )
        val minSongCount = Preferences.minimumSongCountForAlbum
        return splitIntoAlbums(songs).filter {
            it.songCount >= minSongCount
        }
    }

    override fun similarAlbums(album: Album): List<Album> {
        val songCursor = if (!album.albumArtistName.isNullOrEmpty()) {
            songRepository.makeSongCursor(
                "${AudioColumns.ALBUM_ARTIST} = ? AND ${AudioColumns.ALBUM_ID} != ?",
                arrayOf(album.albumArtistName!!, album.id.toString()),
                DEFAULT_SORT_ORDER
            )
        } else {
            songRepository.makeSongCursor(
                "${AudioColumns.ARTIST_ID} = ? AND ${AudioColumns.ALBUM_ID} != ?",
                arrayOf(album.artistId.toString(), album.id.toString()),
                DEFAULT_SORT_ORDER
            )
        }
        val minSongCount = Preferences.minimumSongCountForAlbum
        val songs = songRepository.songs(songCursor)
        return splitIntoAlbums(songs, sortOrder = SortOrder.similarAlbumSortOrder).filter {
            it.songCount >= minSongCount
        }
    }

    // We don't need sorted list of songs (with sortAlbumSongs())
    // cuz we are just displaying Albums(Cover Arts) anyway and not songs
    fun splitIntoAlbums(songs: List<Song>, sorted: Boolean = true, sortOrder: SortOrder = SortOrder.albumSortOrder): List<Album> {
        val grouped = songs.groupBy { it.albumId }.map { Album(it.key, it.value) }
        if (!sorted) return grouped
        return grouped.sortedAlbums(sortOrder)
    }

    private fun sortAlbumSongs(album: Album): Album {
        val songs = album.songs.sortedSongs(SortOrder.albumSongSortOrder)
        return album.copy(songs = songs)
    }

    companion object {
        const val DEFAULT_SORT_ORDER = "${MediaStore.Audio.Albums.ALBUM}, ${MediaStore.Audio.Media.DEFAULT_SORT_ORDER}"
    }
}