
package com.mardous.booming.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.extensions.EXTRA_SONG
import com.mardous.booming.extensions.create
import com.mardous.booming.extensions.withArgs
import com.mardous.booming.fragments.lyrics.LyricsViewModel
import com.mardous.booming.fragments.player.base.goToDestination
import com.mardous.booming.model.Song
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LyricsDialog : DialogFragment() {

    private val lyricsViewModel: LyricsViewModel by activityViewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val song = BundleCompat.getParcelable(requireArguments(), EXTRA_SONG, Song::class.java)!!
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(song.title)
            .setMessage(R.string.no_lyrics_found)
            .setPositiveButton(R.string.open_lyrics_editor) { _: DialogInterface, _: Int ->
                goToDestination(requireActivity(), R.id.nav_lyrics)
            }
            .create { dialog ->
                lyricsViewModel.getLyrics(song, true).observe(this) { result ->
                    if (result.hasData) {
                        dialog.setMessage(result.data)
                    }
                }
            }
    }

    companion object {
        fun create(song: Song) = LyricsDialog().withArgs {
            putParcelable(EXTRA_SONG, song)
        }
    }
}