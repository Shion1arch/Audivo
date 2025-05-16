
package com.mardous.booming.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class InclExclEntity(
    @PrimaryKey
    val path: String,
    val type: Int
)
