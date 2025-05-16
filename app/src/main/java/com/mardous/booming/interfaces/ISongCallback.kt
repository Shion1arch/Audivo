
package com.mardous.booming.interfaces

import android.view.MenuItem
import android.view.View
import com.mardous.booming.model.Song

interface ISongCallback {
    fun songMenuItemClick(song: Song, menuItem: MenuItem, sharedElements: Array<Pair<View, String>>?): Boolean
    fun songsMenuItemClick(songs: List<Song>, menuItem: MenuItem)
}