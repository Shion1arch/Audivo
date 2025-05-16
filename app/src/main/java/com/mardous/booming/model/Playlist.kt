
package com.mardous.booming.model

import android.content.Context
import android.database.Cursor
import android.os.Parcelable
import android.provider.MediaStore
import com.mardous.booming.extensions.media.songsStr
import com.mardous.booming.repository.PlaylistRepository
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

@Parcelize
open class Playlist(val id: Long, val name: String) : Parcelable, KoinComponent {

    @Suppress("DEPRECATION")
    constructor(cursor: Cursor) : this(
        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists._ID)),
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Playlists.NAME))
    )

    open fun description(context: Context): String {
        val songCount = getSongs().size
        return songCount.songsStr(context)
    }

    open fun getSongs(): List<Song> {
        val playlistRepository = get<PlaylistRepository>()
        return playlistRepository.devicePlaylistSongs(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val playlist = other as Playlist
        if (id != playlist.id) return false
        return name == playlist.name
    }

    override fun hashCode(): Int {
        var result = id.toInt()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "Playlist{id=$id, name='$name'}"
    }

    companion object {
        val EmptyPlaylist = Playlist(-1, "")
    }
}