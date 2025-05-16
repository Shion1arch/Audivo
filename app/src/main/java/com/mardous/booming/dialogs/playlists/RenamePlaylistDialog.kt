
package com.mardous.booming.dialogs.playlists

import android.content.DialogInterface
import com.mardous.booming.R
import com.mardous.booming.database.PlaylistEntity
import com.mardous.booming.dialogs.InputDialog
import com.mardous.booming.extensions.extraNotNull
import com.mardous.booming.extensions.withArgs
import com.mardous.booming.fragments.LibraryViewModel
import com.mardous.booming.fragments.ReloadType
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class RenamePlaylistDialog : InputDialog() {

    private val libraryViewModel: LibraryViewModel by activityViewModel()
    private val playlistEntity: PlaylistEntity by extraNotNull(EXTRA_PLAYLIST)

    override fun inputConfig(): InputConfig {
        return Builder(requireContext())
            .title(R.string.rename_playlist_title)
            .hint(R.string.playlist_name_empty)
            .prefill(playlistEntity.playlistName)
            .positiveText(R.string.rename_action)
            .createConfig()
    }

    override fun processInput(
        dialog: DialogInterface,
        text: String?,
        isChecked: Boolean
    ): Boolean {
        val playlistName = text?.trim()
        if (!playlistName.isNullOrEmpty()) {
            libraryViewModel.renamePlaylist(playlistEntity.playListId, playlistName)
            libraryViewModel.forceReload(ReloadType.Playlists)
            return true
        } else {
            inputLayout().error = getString(R.string.playlist_name_cannot_be_empty)
            return false
        }
    }

    companion object {
        private const val EXTRA_PLAYLIST = "extra_playlist"

        fun create(playlist: PlaylistEntity) =
            RenamePlaylistDialog().withArgs { putParcelable(EXTRA_PLAYLIST, playlist) }
    }
}