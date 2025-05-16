
package com.mardous.booming.model

import androidx.annotation.StringRes
import com.mardous.booming.R

enum class ContentType(@StringRes internal val titleRes: Int) {
    TopArtists(R.string.top_artists),
    RecentArtists(R.string.recent_artists),
    TopAlbums(R.string.top_albums),
    RecentAlbums(R.string.recent_albums),
    TopTracks(R.string.top_tracks_label),
    History(R.string.history_label),
    RecentSongs(R.string.last_added_label),
    Favorites(R.string.favorites_label),
    NotRecentlyPlayed(R.string.not_recently_played);

    val isPlayableContent: Boolean
        get() = this == Favorites || this == History || this == TopTracks || this == RecentSongs || this == NotRecentlyPlayed

    val isHistoryContent: Boolean
        get() = this == History

    val isFavoriteContent: Boolean
        get() = this == Favorites

    val isRecentContent: Boolean
        get() = this == RecentSongs || this == RecentAlbums || this == RecentArtists

    val isSearchableContent: Boolean
        get() = isFavoriteContent || isRecentContent
}