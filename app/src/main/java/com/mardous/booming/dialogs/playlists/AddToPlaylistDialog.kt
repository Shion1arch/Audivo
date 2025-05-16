
package com.mardous.booming.dialogs.playlists

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.adapters.AddToPlaylistAdapter
import com.mardous.booming.database.PlaylistWithSongs
import com.mardous.booming.databinding.DialogProgressRecyclerViewBinding
import com.mardous.booming.extensions.*
import com.mardous.booming.fragments.LibraryViewModel
import com.mardous.booming.model.Song
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class AddToPlaylistDialog : DialogFragment(), AddToPlaylistAdapter.IAddToPlaylistCallback {

    private val libraryViewModel: LibraryViewModel by activityViewModel()
    private val songs by extraNotNull<List<Song>>(EXTRA_SONGS)

    private lateinit var adapter: AddToPlaylistAdapter
    private lateinit var binding: DialogProgressRecyclerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = AddToPlaylistAdapter(
            requireActivity() as AppCompatActivity,
            Glide.with(this),
            this
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogProgressRecyclerViewBinding.inflate(layoutInflater)
        binding.recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.add_playlist_title)
            .setView(binding.root)
            .setPositiveButton(R.string.close_action, null)
            .create {
                libraryViewModel.playlistsAsync().observe(this) { playlists ->
                    binding.progressIndicator.hide()
                    adapter.data(playlists)
                    binding.recyclerView.scheduleLayoutAnimation()
                }
            }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val fragment = childFragmentManager.findFragmentByTag("CREATE_PLAYLIST")
        if (fragment is DialogFragment) {
            fragment.dismiss()
        }
    }

    override fun newPlaylistClick() {
        val dialog = CreatePlaylistDialog.create(songs)
        dialog.callback(object : CreatePlaylistDialog.PlaylistCreatedCallback {
            override fun playlistCreated() {
                dismiss()
            }
        })
        dialog.show(childFragmentManager, "CREATE_PLAYLIST")
    }

    override fun playlistClick(playlist: PlaylistWithSongs) {
        val playlistName = playlist.playlistEntity.playlistName
        libraryViewModel.addToPlaylist(playlistName, songs).observe(this) {
            if (it.isWorking) {
                adapter.adding(playlist.playlistEntity.playListId)
            } else {
                if (it.insertedSongs > 1) {
                    showToast(
                        getString(
                            R.string.inserted_x_songs_into_playlist_x,
                            it.insertedSongs,
                            it.playlistName
                        )
                    )
                } else if (it.insertedSongs == 1) {
                    showToast(
                        getString(
                            R.string.inserted_one_song_into_playlist_x,
                            it.playlistName
                        )
                    )
                }
                adapter.adding(-1)
            }
        }
    }

    companion object {
        fun create(song: Song) = create(listOf(song))

        fun create(songs: List<Song>): AddToPlaylistDialog {
            return AddToPlaylistDialog().withArgs {
                putParcelableArrayList(EXTRA_SONGS, ArrayList(songs))
            }
        }
    }
}