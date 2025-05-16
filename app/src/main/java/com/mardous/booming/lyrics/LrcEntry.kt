package com.mardous.booming.lyrics

import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import com.mardous.booming.extensions.utilities.formatTime


class LrcEntry(
    val time: Long,
    val text: String
) : Comparable<LrcEntry?> {

    var staticLayout: StaticLayout? = null
        private set

    @JvmField
    var offset: Float = Float.MIN_VALUE
    val height: Int
        get() {
            if (staticLayout == null) {
                return 0
            }
            return staticLayout!!.height
        }

    fun init(paint: TextPaint, width: Int, gravity: Int) {
        val align = when (gravity) {
            GRAVITY_LEFT -> Layout.Alignment.ALIGN_NORMAL
            GRAVITY_CENTER -> Layout.Alignment.ALIGN_CENTER
            GRAVITY_RIGHT -> Layout.Alignment.ALIGN_OPPOSITE
            else -> Layout.Alignment.ALIGN_CENTER
        }

        staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, paint, width)
            .setAlignment(align)
            .setEllipsize(TextUtils.TruncateAt.END)
            .setLineSpacing(0f, 1.25f)
            .setIncludePad(false)
            .build()

        offset = Float.MIN_VALUE
    }

    fun getFormattedText(): String =
        "[${time.formatTime()}] $text"

    override fun compareTo(other: LrcEntry?): Int {
        if (other == null) {
            return -1
        }
        return (time - other.time).toInt()
    }

    companion object {
        const val GRAVITY_CENTER: Int = 0
        const val GRAVITY_LEFT: Int = 1
        const val GRAVITY_RIGHT: Int = 2
    }
}
