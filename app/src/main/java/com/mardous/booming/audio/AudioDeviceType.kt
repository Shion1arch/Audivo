
package com.mardous.booming.audio

import android.annotation.SuppressLint
import android.media.AudioDeviceInfo
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mardous.booming.R

@SuppressLint("InlinedApi")
enum class AudioDeviceType(
    @DrawableRes val iconRes: Int,
    @StringRes internal val nameRes: Int,
    internal val isProduct: Boolean,
    internal val types: Array<Int>
) {
    Aux(
        R.drawable.ic_speaker_24dp, R.string.audio_device_aux_line, false,
        arrayOf(AudioDeviceInfo.TYPE_AUX_LINE)
    ),
    Bluetooth(
        R.drawable.ic_media_bluetooth_on_24dp, R.string.audio_device_bluetooth, true,
        emptyArray()
    ),
    BluetoothA2dp(
        R.drawable.ic_media_bluetooth_on_24dp, R.string.audio_device_bluetooth_a2dp, true,
        arrayOf(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
    ),
    BluetoothSco(
        R.drawable.ic_media_bluetooth_on_24dp, R.string.audio_device_bluetooth_sco, true,
        arrayOf(AudioDeviceInfo.TYPE_BLUETOOTH_SCO)
    ),
    Speaker(
        R.drawable.ic_speaker_24dp, R.string.audio_device_speaker, false,
        emptyArray()
    ),
    BuiltinSpeaker(
        R.drawable.ic_speaker_phone_24dp, R.string.audio_device_builtin_speaker, false,
        arrayOf(
            AudioDeviceInfo.TYPE_BUILTIN_SPEAKER,
            AudioDeviceInfo.TYPE_BUILTIN_SPEAKER_SAFE,
            AudioDeviceInfo.TYPE_BUILTIN_EARPIECE
        )
    ),
    Hdmi(
        R.drawable.ic_hdmi_24dp, R.string.audio_device_hdmi, false,
        arrayOf(AudioDeviceInfo.TYPE_HDMI, AudioDeviceInfo.TYPE_HDMI_ARC)
    ),
    Headset(
        R.drawable.ic_headphones_24dp, R.string.audio_device_headset, false,
        arrayOf(AudioDeviceInfo.TYPE_WIRED_HEADSET, AudioDeviceInfo.TYPE_WIRED_HEADPHONES)
    ),
    UsbDevice(
        R.drawable.ic_usb_24dp, R.string.audio_device_usb_device, true,
        arrayOf(AudioDeviceInfo.TYPE_USB_ACCESSORY, AudioDeviceInfo.TYPE_USB_DEVICE)
    ),
    UsbHeadset(
        R.drawable.ic_usb_24dp, R.string.audio_device_usb_headset, true,
        arrayOf(AudioDeviceInfo.TYPE_USB_HEADSET)
    ),
    Unknown(
        R.drawable.ic_volume_up_24dp, R.string.audio_device_default, false,
        arrayOf(AudioDeviceInfo.TYPE_UNKNOWN)
    );
}

fun AudioDeviceInfo?.getDeviceType() =
    AudioDeviceType.entries.firstOrNull { it.types.contains(this?.type ?: 0) } ?: AudioDeviceType.Unknown