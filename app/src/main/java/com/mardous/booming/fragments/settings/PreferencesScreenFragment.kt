
package com.mardous.booming.fragments.settings

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.mardous.booming.BuildConfig
import com.mardous.booming.R
import com.mardous.booming.extensions.Space
import com.mardous.booming.extensions.applyBottomWindowInsets
import com.mardous.booming.extensions.dip
import com.mardous.booming.extensions.hasS
import com.mardous.booming.extensions.utilities.toEnum
import com.mardous.booming.preferences.dialog.*

open class PreferencesScreenFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        val dialogFragment: DialogFragment? = when (preference) {
            is NowPlayingExtraInfoPreference -> NowPlayingExtraInfoPreferenceDialog()
            is CategoriesPreference -> CategoriesPreferenceDialog()
            is NowPlayingScreenPreference -> NowPlayingScreenPreferenceDialog()
            is ActionOnCoverPreference -> ActionOnCoverPreferenceDialog.newInstance(preference.key, preference.title!!)
            is PreAmpPreference -> PreAmpPreferenceDialog()
            else -> null
        }

        if (dialogFragment != null) {
            dialogFragment.show(childFragmentManager, "androidx.preference.PreferenceFragment.DIALOG")
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDivider(Color.TRANSPARENT.toDrawable())
        if (hasS()) {
            listView.overScrollMode = View.OVER_SCROLL_NEVER
        }

        listView.applyBottomWindowInsets(addedSpace = Space.bottom(dip(R.dimen.mini_player_height)))

        findPreference<Preference>("about")?.summary =
            getString(R.string.about_summary, BuildConfig.VERSION_NAME)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        val settingsScreen = preference.key.toEnum<SettingsScreen>()
        if (settingsScreen != null) {
            findNavController().navigate(settingsScreen.navAction, bundleOf(EXTRA_SCREEN to settingsScreen))
        } else if (preference.key == "about") {
            findNavController().navigate(R.id.action_to_about)
        }
        return true
    }

    protected fun restartActivity() {
        activity?.recreate()
    }

    companion object {
        private const val EXTRA_SCREEN = "extra_screen"
    }
}