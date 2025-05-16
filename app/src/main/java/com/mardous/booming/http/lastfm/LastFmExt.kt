
package com.mardous.booming.http.lastfm

import java.util.EnumMap
import java.util.Locale

fun List<LastFmImage>.getLargestImageUrl(): String? {
    val imageUrls: MutableMap<ImageSize, String?> = EnumMap(ImageSize::class.java)
    for (image in this) {
        var size: ImageSize? = null
        val attribute = image.size
        if (attribute == null) {
            size = ImageSize.UNKNOWN
        } else {
            try {
                size = ImageSize.valueOf(attribute.lowercase(Locale.ENGLISH))
            } catch (e: IllegalArgumentException) {
                // if they suddenly again introduce a new image size
            }
        }
        if (size != null) {
            imageUrls[size] = image.text
        }
    }
    return getLargestImageUrl(imageUrls)
}

private fun getLargestImageUrl(imageUrls: Map<ImageSize, String?>): String? {
    return when {
        imageUrls.containsKey(ImageSize.MEGA) -> imageUrls[ImageSize.MEGA]
        imageUrls.containsKey(ImageSize.EXTRALARGE) -> imageUrls[ImageSize.EXTRALARGE]
        imageUrls.containsKey(ImageSize.LARGE) -> imageUrls[ImageSize.LARGE]
        imageUrls.containsKey(ImageSize.MEDIUM) -> imageUrls[ImageSize.MEDIUM]
        imageUrls.containsKey(ImageSize.SMALL) -> imageUrls[ImageSize.SMALL]
        else -> if (imageUrls.containsKey(ImageSize.UNKNOWN)) {
            imageUrls[ImageSize.UNKNOWN]
        } else null
    }
}

private enum class ImageSize {
    SMALL, MEDIUM, LARGE, EXTRALARGE, MEGA, UNKNOWN
}