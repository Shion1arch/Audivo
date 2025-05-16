
package com.mardous.booming.interfaces

import android.view.MenuItem
import com.mardous.booming.database.PlaylistWithSongs

interface IPlaylistCallback {
    fun playlistClick(playlist: PlaylistWithSongs)
    fun playlistMenuItemClick(playlist: PlaylistWithSongs, menuItem: MenuItem): Boolean
    fun playlistsMenuItemClick(playlists: List<PlaylistWithSongs>, menuItem: MenuItem)
}