

package com.mardous.booming.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.mardous.booming.R
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Parcelize
@Serializable
class CategoryInfo(val category: Category, var visible: Boolean) : Parcelable {

    @Serializable
    enum class Category(
        @IdRes val id: Int,
        @StringRes val titleRes: Int,
        @DrawableRes val iconRes: Int
    ) {
        Home(R.id.nav_home, R.string.for_you_label, R.drawable.icon_home),
        Songs(R.id.nav_songs, R.string.songs_label, R.drawable.icon_music),
        Albums(R.id.nav_albums, R.string.albums_label, R.drawable.icon_album),
        Artists(R.id.nav_artists, R.string.artists_label, R.drawable.icon_artist),
        Playlists(R.id.nav_playlists, R.string.playlists_label, R.drawable.icon_playlist),
        Genres(R.id.nav_genres, R.string.genres_label, R.drawable.icon_genre),
        Years(R.id.nav_years, R.string.release_years_label, R.drawable.icon_year),
        Folders(R.id.nav_file_explorer, R.string.folders_label, R.drawable.icon_folder)
    }

    companion object {
        const val MAX_VISIBLE_CATEGORIES = 5
    }
}