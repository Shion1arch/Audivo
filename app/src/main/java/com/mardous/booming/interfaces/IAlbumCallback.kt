
package com.mardous.booming.interfaces

import android.view.MenuItem
import android.view.View
import com.mardous.booming.model.Album

interface IAlbumCallback {
    fun albumClick(album: Album, sharedElements: Array<Pair<View, String>>?)
    fun albumMenuItemClick(album: Album, menuItem: MenuItem, sharedElements: Array<Pair<View, String>>?): Boolean
    fun albumsMenuItemClick(albums: List<Album>, menuItem: MenuItem)
}