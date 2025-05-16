
package com.mardous.booming.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaRouter
import androidx.core.content.ContextCompat
import androidx.media.AudioManagerCompat
import com.mardous.booming.service.MusicPlayer


class AudioOutputObserver(
    private val context: Context,
    private var callback: Callback?
) : BroadcastReceiver() {

    var audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var mediaRouter: MediaRouter = context.getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter

    private var isObserving = false

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action ?: return
        if (action == VOLUME_CHANGED_ACTION || action == Intent.ACTION_HEADSET_PLUG) {
            requestVolume()
        }
    }

    /**
     * Starts observing the current audio output for volume
     * changes or added/removed devices.
     */
    fun startObserver() {
        checkNotNull(callback) { "Callback not registered" }

        if (!isObserving) {
            val filter = IntentFilter().apply {
                addAction(VOLUME_CHANGED_ACTION)
                addAction(Intent.ACTION_HEADSET_PLUG)
            }
            ContextCompat.registerReceiver(context, this, filter, ContextCompat.RECEIVER_EXPORTED)
            audioManager.registerAudioDeviceCallback(audioDeviceCallback, null)
            this.isObserving = true
        }
    }

    /**
     * Stops observing the current audio output.
     */
    fun stopObserver(removeCallback: Boolean = true) {
        if (removeCallback) {
            this.callback = null
        }
        if (isObserving) {
            context.unregisterReceiver(this)
            audioManager.unregisterAudioDeviceCallback(audioDeviceCallback)
            this.isObserving = false
        }
    }

    fun requestVolume() {
        callback?.onVolumeChange(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
            AudioManagerCompat.getStreamMinVolume(audioManager, AudioManager.STREAM_MUSIC),
            AudioManagerCompat.getStreamMaxVolume(audioManager, AudioManager.STREAM_MUSIC))

        callback?.onFixedVolumeStateChange(audioManager.isVolumeFixed)
    }

    fun requestAudioDevice() {
        if (callback != null) {
            val routedDevice = getCurrentAudioDevice()
            callback?.onAudioOutputDeviceChange(routedDevice)
            callback?.onFixedVolumeStateChange(audioManager.isVolumeFixed)
        }
    }

    private fun getCurrentAudioDevice(): AudioDevice {
        var audioDevice = MusicPlayer.routedDevice
        if (audioDevice == null) {
            val route = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO)
            if (route != null && route.isEnabled) {
                val deviceType = when (route.deviceType) {
                    MediaRouter.RouteInfo.DEVICE_TYPE_SPEAKER -> AudioDeviceType.Speaker
                    MediaRouter.RouteInfo.DEVICE_TYPE_BLUETOOTH -> AudioDeviceType.Bluetooth
                    else -> null
                }
                if (deviceType != null) {
                    audioDevice = AudioDevice(route.deviceType, deviceType, route.name, true)
                }
            }
            if (audioDevice == null) {
                audioDevice = AudioDevice.UnknownDevice
            }
        }
        return audioDevice
    }

    private val audioDeviceCallback: AudioDeviceCallback = object : AudioDeviceCallback() {
        override fun onAudioDevicesAdded(addedDevices: Array<out AudioDeviceInfo>?) {
            requestAudioDevice()
            requestVolume()
        }

        override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
            requestAudioDevice()
            requestVolume()
        }
    }

    interface Callback {
        fun onAudioOutputDeviceChange(currentDevice: AudioDevice)

        fun onVolumeChange(newVolume: Int, minVolume: Int, maxVolume: Int)

        fun onFixedVolumeStateChange(isFixed: Boolean)
    }

    companion object {
        private const val VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION"
    }
}