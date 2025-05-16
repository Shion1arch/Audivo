
package com.mardous.booming.search.filters

import android.os.Parcelable
import com.mardous.booming.database.PlaylistEntity
import com.mardous.booming.model.Genre
import com.mardous.booming.model.ReleaseYear
import com.mardous.booming.repository.SearchRepository
import com.mardous.booming.search.SearchFilter
import com.mardous.booming.search.SearchQuery.FilterMode
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Parcelize
class BasicSearchFilter<T : Parcelable>(private val name: String, private val argument: T) : SearchFilter,
    KoinComponent {

    @IgnoredOnParcel
    private val searchRepository: SearchRepository by inject()

    override fun getName(): CharSequence {
        return name
    }

    override fun getCompatibleModes(): List<FilterMode> {
        return listOf(FilterMode.Songs)
    }

    override suspend fun getResults(searchMode: FilterMode, query: String): List<Any> {
        return when (argument) {
            is Genre -> searchRepository.searchGenreSongs(argument, query)
            is ReleaseYear -> searchRepository.searchYearSongs(argument, query)
            is PlaylistEntity -> searchRepository.searchPlaylistSongs(argument, query)
            else -> arrayListOf()
        }
    }
}