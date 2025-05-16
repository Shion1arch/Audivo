
package com.mardous.booming.fragments.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.Preference
import com.mardous.booming.R
import com.mardous.booming.extensions.isTablet
import com.mardous.booming.util.*

class NowPlayingPreferencesFragment : PreferencesScreenFragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen_now_playing)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findPreference<Preference>(ADD_EXTRA_CONTROLS)?.isVisible = !resources.isTablet
        updateNowPlayingScreen()
        updateCoverActions()
        Preferences.registerOnSharedPreferenceChangeListener(this)
    }

    private fun updateNowPlayingScreen() {
        findPreference<Preference>(NOW_PLAYING_SCREEN)?.summary =
            getString(Preferences.nowPlayingScreen.titleRes)
    }

    private fun updateCoverActions() {
        findPreference<Preference>(COVER_DOUBLE_TAP_ACTION)?.summary =
            getString(Preferences.coverDoubleTapAction.titleRes)

        findPreference<Preference>(COVER_LONG_PRESS_ACTION)?.summary =
            getString(Preferences.coverLongPressAction.titleRes)
    }

    override fun onDestroyView() {
        Preferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        when (key) {
            NOW_PLAYING_SCREEN -> updateNowPlayingScreen()
            COVER_DOUBLE_TAP_ACTION,
            COVER_LONG_PRESS_ACTION -> updateCoverActions()
        }
    }
}