
package com.mardous.booming.http.lastfm

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.http.encodeURLParameter

class LastFmService(private val client: HttpClient) {

    private suspend fun HttpClient.lastfm(method: String, block: HttpRequestBuilder.() -> Unit) =
        get("https://ws.audioscrobbler.com/2.0/") {
            parameter("format", "json")
            parameter("autocorrect", 1)
            parameter("api_key", API_KEY)
            parameter("method", method)
            block()
        }

    suspend fun albumInfo(albumName: String, artistName: String, language: String?) =
        client.lastfm("album.getInfo") {
            parameter("lang", language)
            url.encodedParameters.append("album", albumName.encodeURLParameter())
            url.encodedParameters.append("artist", artistName.encodeURLParameter())
        }.body<LastFmAlbum>()

    suspend fun artistInfo(artistName: String, language: String?, cacheControl: String?) =
        client.lastfm("artist.getInfo") {
            parameter("lang", language)
            header(HttpHeaders.CacheControl, cacheControl)
            url.encodedParameters.append("artist", artistName.encodeURLParameter())
        }.body<LastFmArtist>()

    companion object {
        private const val API_KEY = "9f2bc9ba054016022d05eca5f74132cb"
    }
}