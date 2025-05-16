
package com.mardous.booming.extensions

import android.Manifest.permission.BLUETOOTH_CONNECT
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.AudioManager
import androidx.core.content.getSystemService

fun Context.isBluetoothA2dpConnected(): Boolean {
    return if (hasS()) {
        isBluetoothProfileInState(BluetoothProfile.A2DP, BluetoothAdapter.STATE_CONNECTED)
    } else isBluetoothA2dpOn()
}

fun Context.isBluetoothA2dpDisconnected(): Boolean {
    return if (hasS()) {
        isBluetoothProfileInState(BluetoothProfile.A2DP, BluetoothAdapter.STATE_DISCONNECTED)
    } else !isBluetoothA2dpOn()
}

private fun Context.isBluetoothProfileInState(profile: Int, state: Int): Boolean {
    val bluetoothAdapter = getSystemService<BluetoothManager>()?.adapter ?: return false
    if (checkSelfPermission(BLUETOOTH_CONNECT) == PERMISSION_GRANTED) {
        return bluetoothAdapter.getProfileConnectionState(profile) == state
    }
    return false
}

@Suppress("DEPRECATION")
private fun Context.isBluetoothA2dpOn() = getSystemService<AudioManager>()?.isBluetoothA2dpOn == true