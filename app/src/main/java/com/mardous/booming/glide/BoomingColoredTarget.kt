package com.mardous.booming.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.transition.Transition
import com.mardous.booming.appContext
import com.mardous.booming.extensions.resources.defaultFooterColor
import com.mardous.booming.glide.palette.BitmapPaletteTarget
import com.mardous.booming.glide.palette.BitmapPaletteWrapper
import com.mardous.booming.helper.color.MediaNotificationProcessor

abstract class BoomingColoredTarget(view: ImageView) : BitmapPaletteTarget(view) {

    protected val defaultFooterColor: Int
        get() = getView().context.defaultFooterColor()

    abstract fun onColorReady(colors: MediaNotificationProcessor)

    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        onColorReady(MediaNotificationProcessor.errorColor(appContext()))
    }

    override fun onResourceReady(resource: BitmapPaletteWrapper, transition: Transition<in BitmapPaletteWrapper>?) {
        super.onResourceReady(resource, transition)
        MediaNotificationProcessor(appContext()).getPaletteAsync({
            onColorReady(it)
        }, resource.bitmap)
    }
}
