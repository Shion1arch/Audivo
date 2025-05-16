
package com.mardous.booming.model

import androidx.annotation.StringRes
import com.mardous.booming.R
import java.net.URLEncoder
import java.util.Locale

enum class WebSearchEngine(
    @StringRes val nameRes: Int,
    private val baseUrl: String,
    private val localizedUrl: String? = null
) {
    Google(R.string.google, "https://www.google.com/search?q="),
    LastFm(R.string.lastfm, "https://wwww.last.fm/music/", "https://www.last.fm/%s/music/"),
    Wikipedia(R.string.wikipedia, "https://www.wikipedia.org/wiki/Special:Search?search=", "https://%s.wikipedia.org/wiki/Special:Search?search="),
    YouTube(R.string.youtube, "https://www.youtube.com/results?search_query=");

    fun getURLForQuery(query: String, locale: Locale = Locale.getDefault()): String {
        val url =
            if (locale != Locale.ENGLISH && localizedUrl != null) String.format(localizedUrl, locale.language) else baseUrl

        return runCatching { url + URLEncoder.encode(query, "UTF-8") }.getOrDefault(url + query)
    }
}