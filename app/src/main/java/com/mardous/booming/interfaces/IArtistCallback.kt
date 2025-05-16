
package com.mardous.booming.interfaces

import android.view.MenuItem
import android.view.View
import com.mardous.booming.model.Artist

interface IArtistCallback {
    fun artistClick(artist: Artist, sharedElements: Array<Pair<View, String>>?)
    fun artistMenuItemClick(artist: Artist, menuItem: MenuItem, sharedElements: Array<Pair<View, String>>?): Boolean
    fun artistsMenuItemClick(artists: List<Artist>, menuItem: MenuItem)
}