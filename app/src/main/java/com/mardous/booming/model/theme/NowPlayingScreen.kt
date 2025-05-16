
package com.mardous.booming.model.theme

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.mardous.booming.R

enum class NowPlayingScreen(
    @StringRes
    val titleRes: Int,
    @DrawableRes
    val drawableResId: Int,
    @LayoutRes
    val albumCoverLayoutRes: Int?,
    val supportsCoverLyrics: Boolean
) {
    Default(R.string.normal, R.drawable.np_normal, R.layout.fragment_album_cover_default, true),
    FullCover(R.string.full_cover, R.drawable.np_full, R.layout.fragment_album_cover, false),
    Gradient(R.string.gradient, R.drawable.np_gradient, R.layout.fragment_album_cover, true),
    Peek(R.string.peek, R.drawable.np_peek, R.layout.fragment_album_cover_peek, false);
}