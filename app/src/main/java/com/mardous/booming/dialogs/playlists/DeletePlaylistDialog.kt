
package com.mardous.booming.dialogs.playlists

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.database.PlaylistEntity
import com.mardous.booming.database.PlaylistWithSongs
import com.mardous.booming.extensions.EXTRA_PLAYLISTS
import com.mardous.booming.extensions.extraNotNull
import com.mardous.booming.extensions.toHtml
import com.mardous.booming.extensions.withArgs
import com.mardous.booming.fragments.LibraryViewModel
import com.mardous.booming.fragments.ReloadType
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class DeletePlaylistDialog : DialogFragment() {

    private val libraryViewModel: LibraryViewModel by activityViewModel()
    private val playlists: List<PlaylistEntity> by extraNotNull(EXTRA_PLAYLISTS)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val titleRes: Int
        val content: CharSequence
        if (playlists.size > 1) {
            titleRes = R.string.delete_playlists_title
            content = getString(R.string.delete_x_playlists, playlists.size).toHtml()
        } else {
            titleRes = R.string.delete_playlist_title
            content = getString(R.string.delete_playlist_x, playlists[0].playlistName).toHtml()
        }

        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(titleRes)
            .setMessage(content)
            .setPositiveButton(R.string.delete_action) { _: DialogInterface, _: Int ->
                libraryViewModel.deleteSongsFromPlaylist(playlists)
                libraryViewModel.deletePlaylists(playlists)
                libraryViewModel.forceReload(ReloadType.Playlists)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    companion object {
        fun create(playlist: PlaylistWithSongs): DeletePlaylistDialog {
            return create(listOf(playlist))
        }

        fun create(playlists: List<PlaylistWithSongs>): DeletePlaylistDialog {
            val playlistEntities = playlists.map { it.playlistEntity }
            return DeletePlaylistDialog().withArgs {
                putParcelableArrayList(EXTRA_PLAYLISTS, ArrayList(playlistEntities))
            }
        }
    }
}