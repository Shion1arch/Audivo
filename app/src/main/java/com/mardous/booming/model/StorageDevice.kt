
package com.mardous.booming.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

/**
 * Simplified wrapper for [android.os.storage.StorageVolume].
 */
@Parcelize
class StorageDevice(
    val path: String,
    val name: String,
    val iconRes: Int
) : Parcelable {

    val file: File
        get() = File(path)

    override fun toString(): String {
        return "StorageDevice{path='$path', name='$name'}"
    }
}
