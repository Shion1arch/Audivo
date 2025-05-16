
package com.mardous.booming.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.preference.Preference
import com.google.android.material.color.DynamicColors
import com.mardous.booming.R
import com.mardous.booming.appInstance
import com.mardous.booming.extensions.hasR
import com.mardous.booming.extensions.hasS
import com.mardous.booming.preferences.ThemePreference
import com.mardous.booming.util.*

class AppearancePreferencesFragment : PreferencesScreenFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen_appearance)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasR()) {
            findPreference<Preference>("lockscreen_category")?.isVisible = false
        }

        findPreference<ThemePreference>(GENERAL_THEME)?.apply {
            customCallback = object : ThemePreference.Callback {
                override fun onThemeSelected(themeName: String) {
                    Preferences.generalTheme = themeName
                    setDefaultNightMode(Preferences.getDayNightMode(themeName))
                    restartActivity()
                }
            }
        }

        findPreference<Preference>(BLACK_THEME)?.apply {
            setOnPreferenceChangeListener { _, newValue ->
                val themeName = Preferences.getGeneralTheme((newValue as Boolean))
                setDefaultNightMode(Preferences.getDayNightMode(themeName))
                requireActivity().recreate()
                true
            }
        }

        findPreference<Preference>(MATERIAL_YOU)?.apply {
            isVisible = hasS()
            setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    DynamicColors.applyToActivitiesIfAvailable(appInstance())
                }
                requireActivity().recreate()
                true
            }
        }

        findPreference<Preference>(USE_CUSTOM_FONT)?.setOnPreferenceChangeListener { _, _ ->
            requireActivity().recreate()
            true
        }
    }
}