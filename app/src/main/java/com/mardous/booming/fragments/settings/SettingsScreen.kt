
package com.mardous.booming.fragments.settings

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.mardous.booming.R

enum class SettingsScreen(@LayoutRes val layoutRes: Int, @IdRes val navAction: Int) {
    Appearance(R.xml.preferences_screen_appearance, R.id.action_to_appearancePreferences),
    NowPlaying(R.xml.preferences_screen_now_playing, R.id.action_to_nowPlayingPreferences),
    Playback(R.xml.preferences_screen_playback, R.id.action_to_playbackPreferences),
    Metadata(R.xml.preferences_screen_metadata, R.id.action_to_metadataPreferences),
    Library(R.xml.preferences_screen_library, R.id.action_to_libraryPreferences),
    Notification(R.xml.preferences_screen_notification, R.id.action_to_notificationPreferences),
    Update(R.xml.preferences_screen_update, R.id.action_to_updatePreferences),
    Advanced(R.xml.preferences_screen_advanced, R.id.action_to_advancedPreferences);
}