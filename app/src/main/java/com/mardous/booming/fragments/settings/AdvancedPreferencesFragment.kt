
package com.mardous.booming.fragments.settings

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.preference.Preference
import com.mardous.booming.R
import com.mardous.booming.dialogs.MultiCheckDialog
import com.mardous.booming.extensions.files.getFormattedFileName
import com.mardous.booming.helper.BackupContent
import com.mardous.booming.helper.BackupHelper
import com.mardous.booming.util.BACKUP_DATA
import com.mardous.booming.util.LANGUAGE_NAME
import com.mardous.booming.util.RESTORE_DATA
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class AdvancedPreferencesFragment : PreferencesScreenFragment() {

    private lateinit var createBackupLauncher: ActivityResultLauncher<String>
    private lateinit var selectBackupLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_screen_advanced)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createBackupLauncher =
            registerForActivityResult(ActivityResultContracts.CreateDocument("application/*")) { destination ->
                backupData(destination)
            }
        selectBackupLauncher =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { selection ->
                restoreData(selection)
            }

        findPreference<Preference>(LANGUAGE_NAME)?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as? String == "auto") {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newValue as? String))
            }
            true
        }

        findPreference<Preference>(BACKUP_DATA)?.setOnPreferenceClickListener {
            createBackupLauncher.launch(
                getFormattedFileName(
                    "Backup",
                    BackupHelper.BACKUP_EXTENSION
                )
            )
            true
        }

        findPreference<Preference>(RESTORE_DATA)?.setOnPreferenceClickListener {
            selectBackupLauncher.launch(arrayOf("application/*"))
            true
        }
    }

    private fun backupData(destination: Uri?) {
        if (destination != null) {
            GlobalScope.launch {
                BackupHelper.createBackup(requireContext(), destination)
            }
        }
    }

    private fun restoreData(selection: Uri?) {
        if (selection != null) {
            val items = BackupContent.entries.map {
                getString(it.titleRes)
            }
            val multiCheckDialog = MultiCheckDialog.Builder(requireContext())
                .title(R.string.select_content_to_restore)
                .items(items)
                .createDialog { _, whichPos, _ ->
                    val content = BackupContent.entries.filterIndexed { i, _ ->
                        whichPos.contains(i)
                    }
                    GlobalScope.launch {
                        BackupHelper.restoreBackup(requireContext(), selection, content)
                    }
                    true
                }
            multiCheckDialog.show(childFragmentManager, "RESTORE_DIALOG")
        }
    }
}