
package com.mardous.booming.model.smartplaylist

import android.content.Context
import com.mardous.booming.R
import com.mardous.booming.appContext
import com.mardous.booming.extensions.utilities.buildInfoString
import com.mardous.booming.model.Song
import com.mardous.booming.repository.SmartRepository
import com.mardous.booming.util.Preferences
import kotlinx.parcelize.Parcelize
import org.koin.core.component.get

@Parcelize
class LastAddedPlaylist : AbsSmartPlaylist(
    appContext().getString(R.string.last_added_label),
    R.drawable.ic_library_add_24dp
) {
    override fun description(context: Context): String {
        // we must pass our activity context here in order to get
        // a string located for the current LocaleList passed to our
        // AppCompatDelegate.
        return buildInfoString(Preferences.getLastAddedCutoff(context).description, super.description(context))
    }

    override fun getSongs(): List<Song> {
        val smartRepository = get<SmartRepository>()
        return smartRepository.recentSongs()
    }
}