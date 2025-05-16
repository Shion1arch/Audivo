
package com.mardous.booming.misc

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class GainValues(
    val rgAlbum: Float,
    val rgTrack: Float,
    val peakAlbum: Float,
    val peakTrack: Float
) : Parcelable
