
package com.mardous.booming.repository

import android.content.Context
import com.mardous.booming.R
import com.mardous.booming.database.PlaylistEntity
import com.mardous.booming.database.toSongs
import com.mardous.booming.model.Genre
import com.mardous.booming.model.ReleaseYear
import com.mardous.booming.model.Song
import com.mardous.booming.search.SearchFilter
import com.mardous.booming.search.SearchQuery
import com.mardous.booming.util.Preferences

interface SearchRepository {
    suspend fun searchAll(context: Context, query: SearchQuery, filter: SearchFilter?): List<Any>
    suspend fun searchGenreSongs(genre: Genre, query: String): List<Song>
    suspend fun searchPlaylistSongs(playlist: PlaylistEntity, query: String): List<Song>
    suspend fun searchYearSongs(year: ReleaseYear, query: String): List<Song>
}

class RealSearchRepository(
    private val albumRepository: RealAlbumRepository,
    private val songRepository: RealSongRepository,
    private val artistRepository: RealArtistRepository,
    private val playlistRepository: RealPlaylistRepository,
    private val genreRepository: GenreRepository,
    private val specialRepository: SpecialRepository
) : SearchRepository {

    override suspend fun searchAll(context: Context, query: SearchQuery, filter: SearchFilter?): List<Any> {
        val results = ArrayList<Any>()
        if (!query.searched.isNullOrEmpty()) {
            if (filter != null) {
                if (query.filterMode != null) {
                    val filteredResults = filter.getResults(query.filterMode, query.searched)
                    if (filteredResults.isNotEmpty()) {
                        results.addAll(filteredResults)
                    }
                }
                // we do nothing if there is a filter but search mode is not valid
            } else {
                val isOnlyAlbumArtists = Preferences.onlyAlbumArtists
                when (query.filterMode) {
                    SearchQuery.FilterMode.Songs -> results.addAll(getSongs(query.searched))
                    SearchQuery.FilterMode.Albums -> results.addAll(getAlbums(query.searched))
                    SearchQuery.FilterMode.Artists -> results.addAll(getArtists(query.searched, isOnlyAlbumArtists))
                    SearchQuery.FilterMode.Genres -> results.addAll(getGenres(query.searched))
                    SearchQuery.FilterMode.Playlists -> results.addAll(getPlaylists(query.searched))
                    else -> {
                        results.addTitled(getSongs(query.searched), context.getString(R.string.songs_label))
                        results.addTitled(
                            getArtists(query.searched, isOnlyAlbumArtists),
                            if (isOnlyAlbumArtists)
                                context.getString(R.string.album_artists_label)
                            else context.getString(R.string.artists_label)
                        )
                        results.addTitled(getAlbums(query.searched), context.getString(R.string.albums_label))
                        results.addTitled(getGenres(query.searched), context.getString(R.string.genres_label))
                        results.addTitled(getPlaylists(query.searched), context.getString(R.string.playlists_label))
                    }
                }
            }
        }
        return results
    }

    override suspend fun searchGenreSongs(genre: Genre, query: String): List<Song> =
        genreRepository.songs(genre.id, query)

    override suspend fun searchPlaylistSongs(playlist: PlaylistEntity, query: String): List<Song> =
        playlistRepository.searchPlaylistSongs(playlist.playListId, query).toSongs()

    override suspend fun searchYearSongs(year: ReleaseYear, query: String): List<Song> =
        specialRepository.songs(year.year, query)

    private fun getSongs(query: String) = songRepository.songs(query)
    private fun getAlbums(query: String) = albumRepository.albums(query)
    private fun getArtists(query: String, isOnlyAlbumArtists: Boolean) =
        if (isOnlyAlbumArtists)
            artistRepository.albumArtists(query)
        else artistRepository.artists(query)

    private suspend fun getGenres(query: String) = genreRepository.genres(query)
    private suspend fun getPlaylists(query: String) = playlistRepository.searchPlaylists(query)
}

internal fun MutableList<Any>.addTitled(results: List<Any>, header: String) {
    if (results.isNotEmpty()) {
        this.add(header)
        this.addAll(results)
    }
}