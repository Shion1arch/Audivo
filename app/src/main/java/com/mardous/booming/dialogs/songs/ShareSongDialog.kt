
package com.mardous.booming.dialogs.songs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.dialogs.ShareStoryDialog
import com.mardous.booming.extensions.*
import com.mardous.booming.model.Song

class ShareSongDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val song = BundleCompat.getParcelable(requireArguments(), EXTRA_SONG, Song::class.java)!!
        val items = arrayOf(
            getString(R.string.the_audio_file),
            getString(R.string.i_am_listening),
            getString(R.string.share_to_stories)
        )
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.what_do_you_want_to_share)
            .setItems(items) { _: DialogInterface, i: Int ->
                when (i) {
                    0 -> startActivity(requireContext().getShareSongIntent(song).toChooser())
                    1 -> startActivity(requireContext().getShareNowPlayingIntent(song).toChooser())
                    2 -> ShareStoryDialog.create(song).show(parentFragmentManager, "SHARE_STORY")
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    companion object {
        fun create(song: Song) = ShareSongDialog().withArgs {
            putParcelable(EXTRA_SONG, song)
        }
    }
}