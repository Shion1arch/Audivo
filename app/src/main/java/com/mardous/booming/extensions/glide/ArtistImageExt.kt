
package com.mardous.booming.extensions.glide

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.edit
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.mardous.booming.appContext
import com.mardous.booming.extensions.resources.getResized
import com.mardous.booming.extensions.resources.toJPG
import com.mardous.booming.extensions.showToast
import com.mardous.booming.model.Artist
import com.mardous.booming.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Locale

private val customImagesPreferences: SharedPreferences by lazy {
    appContext().getSharedPreferences("custom_artist_images", Context.MODE_PRIVATE)
}

private val signaturesPreferences: SharedPreferences by lazy {
    appContext().getSharedPreferences("artist_signatures", Context.MODE_PRIVATE)
}

private fun Artist.getFileName(): String {
    return String.format(Locale.US, "#%d#%s.jpeg", id, name)
}

fun Artist.getCustomImageFile() = File(FileUtil.customArtistImagesDirectory(), getFileName())

// shared prefs saves us many IO operations
fun Artist.hasCustomImage(): Boolean {
    return customImagesPreferences.getBoolean(getFileName(), false)
}

fun Artist.setCustomImage(uri: Uri) {
    Glide.with(appContext())
        .asBitmap()
        .load(uri)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .into(object : CustomTarget<Bitmap?>() {
            override fun onLoadCleared(placeholder: Drawable?) {}

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                MainScope().launch {
                    withContext(Dispatchers.IO) {
                        val dir = FileUtil.customArtistImagesDirectory() ?: return@withContext
                        val file = File(dir, getFileName())

                        val result = runCatching {
                            file.outputStream().buffered().use {
                                resource.getResized(2048).toJPG(100, it)
                            }
                        }

                        if (result.isSuccess) {
                            customImagesPreferences.edit(true) {
                                putBoolean(getFileName(), true)
                            }

                            updateArtistSignature()

                            appContext().contentResolver
                                .notifyChange(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, null) // trigger media store changed to force artist image reload
                        } else {
                            appContext().showToast(result.exceptionOrNull()?.toString())
                        }
                    }
                }
            }
        })
}

fun Artist.resetCustomImage() {
    MainScope().launch {
        withContext(Dispatchers.IO) {
            customImagesPreferences.edit(true) {
                putBoolean(getFileName(), false)
            }

            updateArtistSignature()

            appContext().contentResolver
                .notifyChange(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                    null) // trigger media store changed to force artist image reload

            val file = getCustomImageFile()
            if (file.exists()) {
                file.delete()
            }
        }
    }
}

fun Artist.updateArtistSignature() {
    signaturesPreferences.edit(true) {
        putLong(name, System.currentTimeMillis())
    }
}

fun Artist.getSignatureRaw(): Long {
    return signaturesPreferences.getLong(name, 0)
}

fun Artist.getSignature(): ObjectKey {
    return ObjectKey(getSignatureRaw().toString())
}


