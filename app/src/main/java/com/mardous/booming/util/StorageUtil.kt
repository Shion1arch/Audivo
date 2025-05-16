
package com.mardous.booming.util

import android.annotation.SuppressLint
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import androidx.core.content.getSystemService
import com.mardous.booming.R
import com.mardous.booming.appContext
import com.mardous.booming.extensions.hasR
import com.mardous.booming.model.StorageDevice
import com.mardous.booming.recordException
import java.lang.reflect.InvocationTargetException

object StorageUtil {

    val storageVolumes: List<StorageDevice> by lazy {
        arrayListOf<StorageDevice>().also { newList ->
            try {
                val storageManager = appContext().getSystemService<StorageManager>()!!
                for (sv in storageManager.storageVolumes) {
                    val icon = if (sv.isRemovable && !sv.isPrimary) {
                        R.drawable.ic_sd_card_24dp
                    } else {
                        R.drawable.ic_phone_android_24dp
                    }
                    newList.add(StorageDevice(sv.getPath(), sv.getDescription(appContext()), icon))
                }
            } catch (t: Throwable) {
                recordException(t)
            }
        }
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    @SuppressLint("DiscouragedPrivateApi")
    private fun StorageVolume.getPath(): String {
        return if (hasR()) {
            this.directory!!.absolutePath
        } else {
            StorageVolume::class.java.getDeclaredMethod("getPath").invoke(this) as String
        }
    }
}
