
package com.mardous.booming.appshortcuts

import android.content.Context
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Icon
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.IconCompat
import com.mardous.booming.R
import com.mardous.booming.extensions.getTintedDrawable
import com.mardous.booming.extensions.resources.getColorCompat
import com.mardous.booming.extensions.resources.toBitmap

object AppShortcutIconGenerator {
    fun generateThemedIcon(context: Context, @DrawableRes iconId: Int): Icon {
        val foregroundColor = context.getColorCompat(R.color.app_shortcut_default_foreground)
        val backgroundColor = context.getColorCompat(R.color.app_shortcut_default_background)
        // Get and tint foreground and background drawables
        val vectorDrawable = context.getTintedDrawable(iconId, foregroundColor)
        val backgroundDrawable = context.getTintedDrawable(R.drawable.ic_app_shortcut_background, backgroundColor)
        return IconCompat.createWithAdaptiveBitmap(
            AdaptiveIconDrawable(backgroundDrawable, vectorDrawable).toBitmap()
        ).toIcon(context)
    }
}