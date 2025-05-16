
package com.mardous.booming.model.smartplaylist

import com.mardous.booming.R
import com.mardous.booming.appContext
import com.mardous.booming.model.Song
import com.mardous.booming.repository.SmartRepository
import kotlinx.parcelize.Parcelize
import org.koin.core.component.get

@Parcelize
class TopTracksPlaylist : AbsSmartPlaylist(
    appContext().getString(R.string.shuffle_all_label),
    R.drawable.ic_trending_up_24dp
) {
    override fun getSongs(): List<Song> {
        val smartRepository = get<SmartRepository>()
        return smartRepository.topPlayedSongs()
    }
}