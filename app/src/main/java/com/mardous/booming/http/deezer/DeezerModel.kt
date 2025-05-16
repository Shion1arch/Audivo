
package com.mardous.booming.http.deezer

import com.mardous.booming.util.ImageSize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeezerAlbum(
    @SerialName("data")
    val data: List<AlbumData>
) {
    val imageUrl: String?
        get() = data.firstOrNull()?.let { it.largeImage ?: it.mediumImage ?: it.smallImage }

    @Serializable
    data class AlbumData(
        @SerialName("cover_small")
        val smallImage: String?,
        @SerialName("cover_medium")
        val mediumImage: String?,
        @SerialName("cover_big")
        val largeImage: String?
    )
}

@Serializable
data class DeezerTrack(
    @SerialName("data")
    val data: List<TrackData>
) {
    val imageUrl: String?
        get() = data.firstOrNull()?.let {
            it.album.largeImage ?: it.album.mediumImage ?: it.album.smallImage
        }

    @Serializable
    data class TrackData(
        @SerialName("album")
        val album: Album
    ) {
        @Serializable
        data class Album(
            @SerialName("cover_small")
            val smallImage: String?,
            @SerialName("cover_medium")
            val mediumImage: String?,
            @SerialName("cover_big")
            val largeImage: String?
        )
    }
}

@Serializable
class DeezerArtist(
    @SerialName("data")
    val result: List<Result>,
    val total: Int
) {

    fun getImageUrl(requestedImageSize: String): String? {
        return result.firstOrNull()?.let {
            when (requestedImageSize) {
                ImageSize.LARGE -> it.largeImage ?: it.mediumImage
                ImageSize.SMALL -> it.smallImage ?: it.mediumImage
                else -> it.mediumImage
            }
        }?.takeIf { it.isNotBlank() && !it.contains("/images/artist//") }
    }

    @Serializable
    class Result(
        @SerialName("picture_small")
        val smallImage: String?,
        @SerialName("picture_medium")
        val mediumImage: String?,
        @SerialName("picture_big")
        val largeImage: String?
    )
}