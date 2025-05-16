
package com.mardous.booming.http.github

import android.content.Context
import com.mardous.booming.R
import com.mardous.booming.extensions.isOnline
import com.mardous.booming.util.Preferences
import com.mardous.booming.util.UpdateSearchMode
import java.util.concurrent.TimeUnit

fun Context.isAbleToUpdate(): Boolean {
    if (!resources.getBoolean(R.bool.enable_app_update))
        return false

    val minElapsedMillis = when (Preferences.updateSearchMode) {
        UpdateSearchMode.EVERY_DAY -> TimeUnit.DAYS.toMillis(1)
        UpdateSearchMode.EVERY_FIFTEEN_DAYS -> TimeUnit.DAYS.toMillis(15)
        UpdateSearchMode.WEEKLY -> TimeUnit.DAYS.toMillis(7)
        UpdateSearchMode.MONTHLY -> TimeUnit.DAYS.toMillis(30)
        else -> -1
    }
    val elapsedMillis = System.currentTimeMillis() - Preferences.lastUpdateSearch
    if ((minElapsedMillis > -1) && elapsedMillis >= minElapsedMillis) {
        return isOnline(Preferences.updateOnlyWifi)
    }
    return false
}