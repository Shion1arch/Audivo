
package com.mardous.booming.glide.artistimage

import android.content.Context
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.mardous.booming.extensions.isAllowedToDownloadMetadata
import com.mardous.booming.extensions.media.albumCoverUri
import com.mardous.booming.extensions.media.isNameUnknown
import com.mardous.booming.http.deezer.DeezerService
import com.mardous.booming.util.Preferences
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.isSuccess
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.*
import okhttp3.internal.closeQuietly
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class ArtistImageFetcher(
    private val context: Context,
    private val deezerService: DeezerService,
    private val httpClient: HttpClient,
    private val model: ArtistImage
) : DataFetcher<InputStream> {

    private val downloadArtistImages = Preferences.downloadArtistImages
    private val preferredImageSize = Preferences.preferredArtistImageSize

    private var job: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var mediaStoreStream: InputStream? = null

    @Volatile
    private var isCancelled = false

    private val isAllowedToDownloadImage: Boolean
        get() = !model.artist.isNameUnknown() &&
                downloadArtistImages && context.isAllowedToDownloadMetadata()

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        job = coroutineScope.launch {
            try {
                if (isAllowedToDownloadImage) {
                    val deezerArtist = deezerService.artist(model.artist.name)
                    val imageUrl = deezerArtist?.getImageUrl(preferredImageSize)
                    if (imageUrl != null) {
                        val inputStream = fetchImageInputStream(imageUrl)
                        withContext(Dispatchers.Main) {
                            callback.onDataReady(inputStream)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            callback.onDataReady(getFallbackAlbumImage())
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callback.onDataReady(getFallbackAlbumImage())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    callback.onLoadFailed(e)
                }
            }
        }
    }

    private suspend fun fetchImageInputStream(imageUrl: String): InputStream? {
        return try {
            val response = httpClient.get(imageUrl)
            if (!response.status.isSuccess()) {
                throw IOException("Request failed with code: ${response.status.value}")
            }
            response.bodyAsChannel().toInputStream()
        } catch (e: Exception) {
            throw IOException("Error during Ktor image request", e)
        }
    }

    private fun getFallbackAlbumImage(): InputStream? {
        model.artist.safeGetFirstAlbum().id.let { id ->
            return if (id != -1L) {
                val imageUri = id.albumCoverUri()
                try {
                    context.contentResolver.openInputStream(imageUri)
                } catch (e: FileNotFoundException) {
                    null
                } catch (e: UnsupportedOperationException) {
                    null
                }
            } else {
                null
            }
        }
    }

    override fun cleanup() {
        job?.cancel()
        mediaStoreStream?.closeQuietly()
    }

    override fun cancel() {
        isCancelled = true
        job?.cancel()
    }
}