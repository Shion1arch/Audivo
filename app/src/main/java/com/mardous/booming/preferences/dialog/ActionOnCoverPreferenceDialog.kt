
package com.mardous.booming.preferences.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.model.NowPlayingAction
import com.mardous.booming.util.COVER_DOUBLE_TAP_ACTION
import com.mardous.booming.util.Preferences
import org.koin.android.ext.android.get

class ActionOnCoverPreferenceDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prefKey = requireArguments().getString(EXTRA_KEY)
        val current = getCurrentAction(prefKey)

        val actions = NowPlayingAction.entries.toMutableList()
        makeCleanActions(prefKey, actions)

        val dialogTitle = arguments?.getCharSequence(EXTRA_TITLE)
        val actionNames = actions.map { getString(it.titleRes) }
        var selectedIndex = actions.indexOf(current).coerceAtLeast(0)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(dialogTitle)
            .setSingleChoiceItems(actionNames.toTypedArray(), selectedIndex) { _: DialogInterface, selected: Int ->
                selectedIndex = selected
            }
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                get<SharedPreferences>().edit()
                    .putString(prefKey, actions[selectedIndex].name)
                    .apply()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    private fun makeCleanActions(prefKey: String?, actions: MutableList<NowPlayingAction>) {
        if (COVER_DOUBLE_TAP_ACTION == prefKey) {
            actions.remove(Preferences.coverLongPressAction)
        } else {
            actions.remove(Preferences.coverDoubleTapAction)
        }
        if (!actions.contains(NowPlayingAction.Nothing)) {
            // "Nothing" must be always available, so if we
            // removed it previously, add it again.
            actions.add(NowPlayingAction.Nothing)
        }
    }

    private fun getCurrentAction(prefKey: String?): NowPlayingAction {
        return if (COVER_DOUBLE_TAP_ACTION == prefKey) {
            Preferences.coverDoubleTapAction
        } else Preferences.coverLongPressAction
    }

    companion object {
        private const val EXTRA_KEY = "extra_key"
        private const val EXTRA_TITLE = "extra_title"

        fun newInstance(preference: String, title: CharSequence): ActionOnCoverPreferenceDialog {
            return ActionOnCoverPreferenceDialog().apply {
                arguments = bundleOf(EXTRA_KEY to preference, EXTRA_TITLE to title)
            }
        }
    }
}