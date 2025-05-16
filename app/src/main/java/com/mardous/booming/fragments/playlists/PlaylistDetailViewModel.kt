
package com.mardous.booming.fragments.playlists

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.mardous.booming.database.PlaylistWithSongs
import com.mardous.booming.database.SongEntity
import com.mardous.booming.repository.PlaylistRepository

class PlaylistDetailViewModel(
    private val playlistRepository: PlaylistRepository,
    private var playlistId: Long
) : ViewModel() {
    fun getSongs(): LiveData<List<SongEntity>> =
        playlistRepository.getSongs(playlistId)

    fun playlistExists(): LiveData<Boolean> =
        playlistRepository.checkPlaylistExists(playlistId)

    fun getPlaylist(): LiveData<PlaylistWithSongs> = playlistRepository.playlistWithSongsObservable(playlistId)
}
