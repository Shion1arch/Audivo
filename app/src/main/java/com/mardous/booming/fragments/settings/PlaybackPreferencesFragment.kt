
package com.mardous.booming.fragments.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import com.mardous.booming.R
import com.mardous.booming.util.Preferences
import com.mardous.booming.util.REPLAYGAIN_PREAMP
import com.mardous.booming.util.REPLAYGAIN_SOURCE_MODE
import com.mardous.booming.util.ReplayGainSourceMode

class PlaybackPreferencesFragment : PreferencesScreenFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen_playback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Preferences.registerOnSharedPreferenceChangeListener(this)
        updatePreAmpState()
    }

    private fun updatePreAmpState() {
        findPreference<Preference>(REPLAYGAIN_PREAMP)?.isEnabled =
            Preferences.replayGainSourceMode != ReplayGainSourceMode.MODE_NONE
    }

    override fun onDestroyView() {
        Preferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        when (key) {
            REPLAYGAIN_SOURCE_MODE -> updatePreAmpState()
        }
    }
}