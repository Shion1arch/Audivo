

package com.mardous.booming.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.mardous.booming.extensions.media.albumArtistName
import com.mardous.booming.model.Song
import kotlinx.serialization.Serializable

@Serializable
@Entity(indices = [Index("song_title", "song_artist", unique = true)])
class LyricsEntity(
    @PrimaryKey
    val id: Long,
    @ColumnInfo(name = "song_title")
    val title: String,
    @ColumnInfo(name = "song_artist")
    val artist: String,
    @ColumnInfo(name = "synced_lyrics")
    val syncedLyrics: String,
    @ColumnInfo(name = "auto_download")
    val autoDownload: Boolean,
    @ColumnInfo(name = "user_cleared")
    val userCleared: Boolean
)

fun Song.toLyricsEntity(syncedLyrics: String, autoDownload: Boolean = false, userCleared: Boolean = false): LyricsEntity {
    return LyricsEntity(id, title, albumArtistName(), syncedLyrics, autoDownload, userCleared)
}