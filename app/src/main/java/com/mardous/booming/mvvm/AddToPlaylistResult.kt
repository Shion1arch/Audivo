
package com.mardous.booming.mvvm

data class AddToPlaylistResult(
    val playlistName: String,
    val isWorking: Boolean = false,
    val playlistCreated: Boolean = false,
    val insertedSongs: Int = 0
)