
package com.mardous.booming.mvvm.soundsettings

data class BalanceLevel(val left: Float, val right: Float)

data class TempoLevel(val speed: Float, val pitch: Float, val isFixedPitch: Boolean) {
    val actualPitch: Float
        get() = if (isFixedPitch) speed else pitch
}