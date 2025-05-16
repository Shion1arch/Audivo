
package com.mardous.booming.model.smartplaylist

import com.mardous.booming.R
import com.mardous.booming.model.Playlist

abstract class AbsSmartPlaylist(name: String, val iconRes: Int = R.drawable.ic_queue_music_24dp) :
    Playlist(PlaylistIdGenerator(name, iconRes), name)