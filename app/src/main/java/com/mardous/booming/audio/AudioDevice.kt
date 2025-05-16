
package com.mardous.booming.audio

import android.content.Context
import android.media.AudioDeviceInfo


class AudioDevice(
    val code: Int,
    val type: AudioDeviceType,
    private val productName: CharSequence?,
    private val isProduct: Boolean = type.isProduct
) {

    fun getDeviceName(context: Context): CharSequence {
        return if (isProduct && !productName.isNullOrEmpty()) productName else context.getString(type.nameRes)
    }

    companion object {
        /**
         * Constant describing an unknown audio device.
         */
        val UnknownDevice = AudioDevice(AudioDeviceInfo.TYPE_UNKNOWN, AudioDeviceType.Unknown, null)
    }
}