
package com.mardous.booming.model

import com.mardous.booming.util.sort.SortOrder
import com.mardous.booming.util.sort.sortedAlbums
import com.mardous.booming.util.sort.sortedSongs
import java.util.Objects

data class Artist(val id: Long, val albums: List<Album>, val isAlbumArtist: Boolean = false) {

    constructor(artistName: String, albums: List<Album>, isAlbumArtist: Boolean = false) :
            this(albums.firstOrNull()?.artistId ?: -1, albums, isAlbumArtist) {
        name = artistName
    }

    var name: String = "-"
        private set
        get() = if (isAlbumArtist) getAlbumArtistName()!! else getArtistName()

    val albumCount: Int
        get() = albums.size

    val songCount: Int
        get() = songs.size

    val songs: List<Song>
        get() = albums.flatMap { it.songs }

    val sortedSongs: List<Song>
        get() = songs.sortedSongs(SortOrder.artistSongSortOrder)

    val sortedAlbums: List<Album>
        get() = albums.sortedAlbums(SortOrder.artistAlbumSortOrder)

    val duration: Long
        get() = albums.sumOf { it.duration }

    fun safeGetFirstAlbum(): Album {
        return albums.firstOrNull() ?: Album.empty
    }

    private fun getArtistName(): String {
        return safeGetFirstAlbum().safeGetFirstSong().artistName
    }

    private fun getAlbumArtistName(): String? {
        return safeGetFirstAlbum().safeGetFirstSong().albumArtistName
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val artist = other as Artist
        return id == artist.id && songs == artist.songs && isAlbumArtist == artist.isAlbumArtist
    }

    override fun hashCode(): Int {
        return Objects.hash(id, songs, isAlbumArtist)
    }

    override fun toString(): String {
        return "Artist{id=$id, albums=$albums, isAlbumArtist=$isAlbumArtist}"
    }

    companion object {
        const val VARIOUS_ARTISTS_DISPLAY_NAME = "Various Artists"
        const val VARIOUS_ARTISTS_ID: Long = -2

        val empty = Artist(-1, arrayListOf())
    }
}