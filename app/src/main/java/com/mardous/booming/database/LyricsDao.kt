
package com.mardous.booming.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface LyricsDao {
    @Upsert
    suspend fun insertLyrics(lyrics: List<LyricsEntity>)

    @Upsert
    suspend fun insertLyrics(lyrics: LyricsEntity)

    @Query("DELETE FROM LyricsEntity WHERE id = :songId")
    suspend fun removeLyrics(songId: Long)

    @Query("DELETE FROM LyricsEntity")
    suspend fun removeLyrics()

    @Query("SELECT * FROM LyricsEntity")
    suspend fun getAllLyrics(): List<LyricsEntity>

    @Query("SELECT * FROM LyricsEntity WHERE id = :songId")
    suspend fun getLyrics(songId: Long): LyricsEntity?
}