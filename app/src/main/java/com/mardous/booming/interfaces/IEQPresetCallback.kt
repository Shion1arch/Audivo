
package com.mardous.booming.interfaces

import com.mardous.booming.model.EQPreset

interface IEQPresetCallback {
    fun eqPresetSelected(eqPreset: EQPreset)
    fun editEQPreset(eqPreset: EQPreset)
    fun deleteEQPreset(eqPreset: EQPreset)
}