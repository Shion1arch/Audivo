
package com.mardous.booming.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.extensions.EXTRA_SONG
import com.mardous.booming.extensions.media.searchQuery
import com.mardous.booming.extensions.openWeb
import com.mardous.booming.extensions.toChooser
import com.mardous.booming.extensions.withArgs
import com.mardous.booming.model.Song
import com.mardous.booming.model.WebSearchEngine

class WebSearchDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val song = BundleCompat.getParcelable(requireArguments(), EXTRA_SONG, Song::class.java)!!

        val engines = WebSearchEngine.entries.toTypedArray()
        val titles = engines.map { getString(it.nameRes) }.toTypedArray()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.where_do_you_want_to_search)
            .setNegativeButton(android.R.string.cancel, null)
            .setItems(titles) { _: DialogInterface, position: Int ->
                startActivity(
                    song.searchQuery(engines[position]).openWeb().toChooser(getString(R.string.web_search))
                )
            }
            .create()
    }

    companion object {
        fun create(song: Song) = WebSearchDialog().withArgs {
            putParcelable(EXTRA_SONG, song)
        }
    }
}