
package com.mardous.booming.dialogs.songs

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.databinding.DialogRingtoneBinding
import com.mardous.booming.extensions.EXTRA_SONG
import com.mardous.booming.extensions.media.configureRingtone
import com.mardous.booming.extensions.resources.animateToggle
import com.mardous.booming.extensions.toHtml
import com.mardous.booming.extensions.withArgs
import com.mardous.booming.model.Song


class SetRingtoneDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val song = BundleCompat.getParcelable(requireArguments(), EXTRA_SONG, Song::class.java)
        checkNotNull(song)

        if (!Settings.System.canWrite(requireContext())) {
            return MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.permissions_denied)
                .setMessage(getString(R.string.permission_request_write_settings, song.title).toHtml())
                .setPositiveButton(R.string.action_grant) { _: DialogInterface, _: Int ->
                    try {
                        startActivity(Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                            data = Uri.fromParts("package", requireContext().packageName, null)
                        })
                    } catch (_: ActivityNotFoundException) {
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .create()
        }

        var binding: DialogRingtoneBinding

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.action_set_as_ringtone)
            .also {
                binding = DialogRingtoneBinding.inflate(layoutInflater.cloneInContext(it.context)).apply {
                    message.text = getString(R.string.x_will_be_set_as_ringtone, song.title).toHtml()
                    checkboxContainer.setOnClickListener {
                        checkbox.animateToggle()
                    }
                }
            }
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                song.configureRingtone(requireContext(), binding.checkbox.isChecked)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    companion object {
        fun create(song: Song): SetRingtoneDialog {
            return SetRingtoneDialog().withArgs {
                putParcelable(EXTRA_SONG, song)
            }
        }
    }
}