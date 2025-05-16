
package com.mardous.booming.glide.artistimage

import android.content.Context
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.mardous.booming.http.deezer.DeezerService
import io.ktor.client.HttpClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.InputStream

class ArtistImageLoader private constructor(
    private val context: Context,
    private val deezerService: DeezerService,
    private val httpClient: HttpClient
) : ModelLoader<ArtistImage, InputStream> {

    override fun buildLoadData(model: ArtistImage, width: Int, height: Int, options: Options): LoadData<InputStream> {
        return LoadData(ObjectKey(model.artist.name), ArtistImageFetcher(context, deezerService, httpClient, model))
    }

    override fun handles(artistImage: ArtistImage): Boolean {
        return true
    }

    class Factory(private val context: Context) : ModelLoaderFactory<ArtistImage, InputStream>, KoinComponent {

        private val httpClient by inject<HttpClient>()
        private val deezerService by inject<DeezerService>()

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ArtistImage, InputStream> {
            return ArtistImageLoader(context, deezerService, httpClient)
        }

        override fun teardown() {}
    }
}