
package com.mardous.booming.mvvm

import android.net.Uri
import androidx.annotation.StringRes
import com.mardous.booming.model.EQPreset

open class PresetOpResult(
    val success: Boolean,
    @StringRes val messageRes: Int = 0,
    val canDismiss: Boolean = true
)

class ExportRequestResult(
    success: Boolean,
    @StringRes messageRes: Int = 0,
    val presets: List<EQPreset> = emptyList(),
    val presetNames: List<String> = emptyList()
) : PresetOpResult(success, messageRes = messageRes)

class PresetExportResult(
    success: Boolean,
    @StringRes messageRes: Int = 0,
    val data: Uri? = null,
    val mimeType: String? = null
) : PresetOpResult(success, messageRes = messageRes)

class ImportRequestResult(
    success: Boolean,
    @StringRes messageRes: Int = 0,
    val presets: List<EQPreset> = emptyList(),
    val presetNames: List<String> = emptyList()
) : PresetOpResult(success, messageRes = messageRes)

class PresetImportResult(
    success: Boolean,
    @StringRes messageRes: Int = 0,
    val imported: Int = 0
) : PresetOpResult(success, messageRes = messageRes)