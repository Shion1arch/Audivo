
package com.mardous.booming.search

import androidx.annotation.IdRes
import com.mardous.booming.R

data class SearchQuery(val filterMode: FilterMode? = null, val searched: String? = null) {

    enum class FilterMode(@IdRes val chipId: Int) {
        Songs(R.id.chip_songs),
        Albums(R.id.chip_albums),
        Artists(R.id.chip_artists),
        Genres(R.id.chip_genres),
        Playlists(R.id.chip_playlists)
    }
}