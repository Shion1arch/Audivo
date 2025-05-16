
package com.mardous.booming.fragments.settings

import android.os.Bundle
import com.mardous.booming.R

class NotificationPreferencesFragment : PreferencesScreenFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen_notification)
    }
}