
package com.mardous.booming.extensions

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ResolveInfoFlags
import android.media.MediaPlayer
import android.os.Build
import androidx.core.text.HtmlCompat
import com.mardous.booming.appContext

fun hasPie() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

fun hasQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

fun hasR() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

fun hasS() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

fun hasT() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

fun PackageManager.packageInfo(packageName: String = appContext().packageName) =
    runCatching {
        if (hasT()) getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        else getPackageInfo(packageName, 0)
    }.getOrNull()

fun PackageManager.resolveActivity(intent: Intent) =
    if (hasT())
        resolveActivity(intent, ResolveInfoFlags.of(0))
    else resolveActivity(intent, 0)

fun CharSequence.toHtml() = HtmlCompat.fromHtml(this.toString(), HtmlCompat.FROM_HTML_MODE_COMPACT)

fun <R> MediaPlayer.execSafe(command: MediaPlayer.() -> R): R? {
    try {
        return command()
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
    }
    return null
}