

package com.mardous.booming.http.deezer

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.encodeURLParameter

class DeezerService(private val client: HttpClient) {
    suspend fun track(artistName: String, title: String) =
        client.get("https://api.deezer.com/search?limit=1") {
            url {
                encodedParameters.append("q", "$artistName $title".encodeURLParameter())
            }
        }.body<DeezerTrack>()

    suspend fun album(artistName: String, name: String) =
        client.get("https://api.deezer.com/search/album?limit=1") {
            url {
                encodedParameters.append("q", "$artistName $name".encodeURLParameter())
            }
        }.body<DeezerAlbum>()

    suspend fun artist(artistName: String) =
        try {
            client.get("https://api.deezer.com/search/artist") {
                url {
                    parameters.append("limit", "1")
                    parameters.append("q", artistName)
                }
            }.body<DeezerArtist>()
        } catch (e: Exception) {
            Log.w("DeezerService", "Couldn't decode Deezer response for $artistName", e)
            null
        }
}