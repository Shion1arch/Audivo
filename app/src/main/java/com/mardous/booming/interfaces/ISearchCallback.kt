
package com.mardous.booming.interfaces

import android.view.MenuItem
import android.view.View
import com.mardous.booming.database.PlaylistWithSongs
import com.mardous.booming.model.Album
import com.mardous.booming.model.Artist
import com.mardous.booming.model.Genre
import com.mardous.booming.model.Song

interface ISearchCallback {
    fun songMenuItemClick(song: Song, menuItem: MenuItem): Boolean
    fun albumClick(album: Album, sharedElements: Array<Pair<View, String>>)
    fun albumMenuItemClick(album: Album, menuItem: MenuItem, sharedElements: Array<Pair<View, String>>): Boolean
    fun artistClick(artist: Artist, sharedElements: Array<Pair<View, String>>)
    fun artistMenuItemClick(artist: Artist, menuItem: MenuItem, sharedElements: Array<Pair<View, String>>): Boolean
    fun playlistClick(playlist: PlaylistWithSongs)
    fun playlistMenuItemClick(playlist: PlaylistWithSongs, menuItem: MenuItem): Boolean
    fun genreClick(genre: Genre)
}