
package com.mardous.booming.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val id: Long,
    val name: String,
    val songCount: Int
) : Parcelable {
    companion object {
        val EmptyGenre = Genre(-1, "", -1)
    }
}