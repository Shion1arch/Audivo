
package com.mardous.booming.service.playback

import android.media.AudioDeviceInfo
import android.os.Build
import androidx.annotation.RequiresApi

interface Playback {

    class RepeatMode {
        companion object {
            const val OFF = 0
            const val CURRENT = 1
            const val ALL = 2
        }
    }

    class ShuffleMode {
        companion object {
            const val OFF = 0
            const val ON = 1
        }
    }

    interface PlaybackCallbacks {
        fun onTrackWentToNext()

        fun onTrackEnded()

        fun onPlayStateChanged()
    }

    fun setDataSource(path: String, completion: (success: Boolean) -> Unit)
    fun setNextDataSource(path: String?)
    fun getCallbacks(): PlaybackCallbacks?
    fun setCallbacks(callbacks: PlaybackCallbacks)
    fun isInitialized(): Boolean
    fun start(): Boolean
    fun stop()
    fun release()
    fun pause(): Boolean
    fun isPlaying(): Boolean
    fun duration(): Int
    fun position(): Int
    fun seek(whereto: Int)
    fun setTempo(speed: Float, pitch: Float)
    fun setBalance(left: Float, right: Float)
    fun setReplayGain(replayGain: Float)
    fun setDuckingFactor(vol: Float)
    fun setVolume(leftVol: Float, rightVol: Float)
    fun setAudioSessionId(sessionId: Int): Boolean
    fun getAudioSessionId(): Int
    fun getSpeed(): Float

    @RequiresApi(Build.VERSION_CODES.P)
    fun getRoutedDevice(): AudioDeviceInfo?
}