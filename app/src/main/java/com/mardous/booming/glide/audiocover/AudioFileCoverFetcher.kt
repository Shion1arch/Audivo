
package com.mardous.booming.glide.audiocover

import android.media.MediaMetadataRetriever
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import okhttp3.internal.closeQuietly
import java.io.ByteArrayInputStream
import java.io.InputStream

class AudioFileCoverFetcher internal constructor(private val model: AudioFileCover) :
    DataFetcher<InputStream> {

    private var stream: InputStream? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream?>) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(model.filePath)
            val picture = retriever.embeddedPicture
            stream = if (picture != null) {
                ByteArrayInputStream(picture)
            } else {
                AudioFileCoverUtils.fallback(model.filePath, model.useFolderArt)
            }
            callback.onDataReady(stream)
        } catch (e: Exception) {
            callback.onLoadFailed(e)
        } finally {
            retriever.release()
        }
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }

    override fun cleanup() {
        stream?.closeQuietly()
    }

    override fun cancel() {
        // cannot cancel
    }
}