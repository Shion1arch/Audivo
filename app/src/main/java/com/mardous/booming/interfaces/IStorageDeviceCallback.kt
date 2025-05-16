
package com.mardous.booming.interfaces

import com.mardous.booming.model.StorageDevice

interface IStorageDeviceCallback {
    fun storageDeviceClick(storage: StorageDevice)
}