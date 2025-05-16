
package com.mardous.booming.service

import android.os.Handler
import kotlinx.coroutines.Runnable

class ThrottledSeekHandler(private val musicService: MusicService, private val mHandler: Handler) :
    Runnable {
    override fun run() {
        musicService.savePositionInTrack()
    }

    fun notifySeek() {
        musicService.updateMediaSessionPlaybackState()
        mHandler.removeCallbacks(this)
        mHandler.postDelayed(this, THROTTLE)
    }

    companion object {
        private const val THROTTLE: Long = 500
    }
}