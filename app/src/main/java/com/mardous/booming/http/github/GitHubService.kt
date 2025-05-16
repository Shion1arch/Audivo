
package com.mardous.booming.http.github

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse

class GitHubService(private val context: Context, private val client: HttpClient, private val authToken: String? = null) {

    private suspend fun get(url: String): HttpResponse {
        return client.get(url) {
            authToken?.let {
                headers { append("Authorization", "token $it") }
            }
        }
    }

    private suspend fun fetchStableRelease(user: String, repo: String): GitHubRelease =
        get("https://api.github.com/repos/$user/$repo/releases/latest").body()

    private suspend fun fetchAllReleases(user: String, repo: String, page: Int = 1, limit: Int = 20): List<GitHubRelease> =
        get("https://api.github.com/repos/$user/$repo/releases?page=$page&per_page=$limit").body()

    suspend fun latestRelease(user: String = DEFAULT_USER, repo: String = DEFAULT_REPO, allowExperimental: Boolean = true): GitHubRelease {
        val stableRelease = fetchStableRelease(user, repo)
        if (stableRelease.hasApk && stableRelease.isNewer(context)) {
            return stableRelease
        }
        if (allowExperimental) {
            val allReleases = fetchAllReleases(user, repo)
                .filter { it.isPrerelease }
                .sortedByDescending { it.publishedAt }
            return allReleases.firstOrNull()
                ?: stableRelease
        }
        return stableRelease
    }

    companion object {
        const val DEFAULT_USER = "mardous"
        const val DEFAULT_REPO = "BoomingMusic"
    }
}