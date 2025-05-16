package com.mardous.booming.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PlayCountDao {

    @Upsert
    fun upsertSongInPlayCount(playCountEntity: PlayCountEntity)

    @Delete
    fun deleteSongInPlayCount(playCountEntity: PlayCountEntity)

    @Query("SELECT * FROM PlayCountEntity WHERE id =:songId LIMIT 1")
    fun findSongExistInPlayCount(songId: Long): PlayCountEntity?

    @Query("SELECT * FROM PlayCountEntity WHERE play_count > 0 ORDER BY play_count DESC")
    fun playCountSongs(): List<PlayCountEntity>

    @Query("SELECT * FROM PlayCountEntity WHERE skip_count > 0 ORDER BY skip_count DESC")
    fun skipCountSongs(): List<PlayCountEntity>

    @Query("DELETE FROM PlayCountEntity")
    fun clearPlayCount()
}
