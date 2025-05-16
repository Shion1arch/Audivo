
package com.mardous.booming.search.filters

import com.mardous.booming.model.ContentType
import com.mardous.booming.repository.RealAlbumRepository
import com.mardous.booming.repository.RealArtistRepository
import com.mardous.booming.repository.SmartRepository
import com.mardous.booming.search.SearchFilter
import com.mardous.booming.search.SearchQuery.FilterMode
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Parcelize
class LastAddedSearchFilter(private val name: CharSequence) : SearchFilter, KoinComponent {

    @IgnoredOnParcel
    private val smartRepository by inject<SmartRepository>()

    @IgnoredOnParcel
    private val albumRepository by inject<RealAlbumRepository>()

    @IgnoredOnParcel
    private val artistRepository by inject<RealArtistRepository>()

    override fun getName(): CharSequence {
        return name
    }

    override fun getCompatibleModes(): List<FilterMode> {
        return listOf(FilterMode.Songs, FilterMode.Albums, FilterMode.Artists)
    }

    override suspend fun getResults(searchMode: FilterMode, query: String): List<Any> {
        val contentType = when (searchMode) {
            FilterMode.Albums -> ContentType.RecentAlbums
            FilterMode.Artists -> ContentType.RecentArtists
            else -> ContentType.RecentSongs
        }
        val songs = smartRepository.recentSongs(query, contentType)
        return when (contentType) {
            ContentType.RecentAlbums -> albumRepository.splitIntoAlbums(songs, false)
            ContentType.RecentArtists -> artistRepository.splitIntoAlbumArtists(albumRepository.splitIntoAlbums(songs))
            else -> songs
        }
    }
}