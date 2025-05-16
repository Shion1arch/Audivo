
package com.mardous.booming.database

import androidx.room.*

@Dao
interface InclExclDao {
    companion object {
        const val WHITELIST = 0
        const val BLACKLIST = 1
    }

    @Query("SELECT * FROM InclExclEntity WHERE type = :type")
    suspend fun getPaths(type: Int): List<InclExclEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPath(blackListStoreEntity: InclExclEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaths(blackListStoreEntities: List<InclExclEntity>)

    @Delete
    suspend fun deletePath(blackListStoreEntity: InclExclEntity)

    @Query("DELETE FROM InclExclEntity WHERE type = :type")
    suspend fun clearPaths(type: Int)

    @Query("SELECT * FROM InclExclEntity WHERE type = 1")
    fun blackListPaths(): List<InclExclEntity>

    @Query("SELECT * FROM InclExclEntity WHERE type = 0")
    fun whitelistPaths(): List<InclExclEntity>
}
