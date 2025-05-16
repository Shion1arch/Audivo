
package com.mardous.booming.service.equalizer

import android.annotation.SuppressLint
import android.content.Context
import android.media.audiofx.AudioEffect
import androidx.core.content.edit
import com.mardous.booming.extensions.files.getFormattedFileName
import com.mardous.booming.model.EQPreset
import com.mardous.booming.model.EQPreset.Companion.getEmptyPreset
import com.mardous.booming.mvvm.equalizer.EqEffectState
import com.mardous.booming.mvvm.equalizer.EqEffectUpdate
import com.mardous.booming.mvvm.equalizer.EqState
import com.mardous.booming.mvvm.equalizer.EqUpdate
import com.mardous.booming.util.PLAYBACK_PITCH
import com.mardous.booming.util.PLAYBACK_SPEED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import java.util.Locale
import java.util.UUID

class EqualizerManager internal constructor(context: Context) {

    private val mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val eqSession = EqualizerSession(context, this)

    private var isEqualizerSupported = false
    private var isVirtualizerSupported = false
    private var isBassBoostSupported = false
    private var isLoudnessEnhancerSupported = false
    private var isPresetReverbSupported = false

    private val _eqStateFlow: MutableStateFlow<EqState>
    private val _bassBoostFlow: MutableStateFlow<EqEffectState<Float>>
    private val _virtualizerFlow: MutableStateFlow<EqEffectState<Float>>
    private val _loudnessGainFlow: MutableStateFlow<EqEffectState<Float>>
    private val _presetReverbFlow: MutableStateFlow<EqEffectState<Int>>
    private val _currentPresetFlow: MutableStateFlow<EQPreset>
    private val _presetsFlow: MutableStateFlow<List<EQPreset>>

    val eqStateFlow: StateFlow<EqState> get() = _eqStateFlow
    val bassBoostFlow: StateFlow<EqEffectState<Float>> get() = _bassBoostFlow
    val virtualizerFlow: StateFlow<EqEffectState<Float>> get() = _virtualizerFlow
    val loudnessGainFlow: StateFlow<EqEffectState<Float>> get() = _loudnessGainFlow
    val presetReverbFlow: StateFlow<EqEffectState<Int>> get() = _presetReverbFlow
    val currentPresetFlow: StateFlow<EQPreset> get() = _currentPresetFlow
    val presetsFlow: StateFlow<List<EQPreset>> get() = _presetsFlow

    val eqState: EqState get() = eqStateFlow.value
    val bassBoostState: EqEffectState<Float> get() = bassBoostFlow.value
    val virtualizerState: EqEffectState<Float> get() = virtualizerFlow.value
    val loudnessGainState: EqEffectState<Float> get() = loudnessGainFlow.value
    val presetReverbState: EqEffectState<Int> get() = presetReverbFlow.value

    val equalizerPresets: List<EQPreset> get() = presetsFlow.value
    val currentPreset: EQPreset get() = currentPresetFlow.value

    var isInitialized: Boolean
        get() = mPreferences.getBoolean(Keys.IS_INITIALIZED, false)
        set(value) = mPreferences.edit {
            putBoolean(Keys.IS_INITIALIZED, value)
        }

    init {
        try {
            //Query available effects
            val effects = AudioEffect.queryEffects()
            //Determine available/supported effects
            if (!effects.isNullOrEmpty()) {
                for (effect in effects) {
                    when (effect.type) {
                        UUID.fromString(EFFECT_TYPE_EQUALIZER) -> isEqualizerSupported = true
                        UUID.fromString(EFFECT_TYPE_BASS_BOOST) -> isBassBoostSupported = true
                        UUID.fromString(EFFECT_TYPE_VIRTUALIZER) -> isVirtualizerSupported = true
                        UUID.fromString(EFFECT_TYPE_LOUDNESS_ENHANCER) -> isLoudnessEnhancerSupported = true
                        //UUID.fromString(EFFECT_TYPE_PRESET_REVERB) -> isPresetReverbSupported = true
                        //We've temporarily disabled PresetReverb because it was not working as expected.
                    }
                }
            }
        } catch (_: NoClassDefFoundError) {
            //The user doesn't have the AudioEffect/AudioEffect.Descriptor class. How sad.
        }

        _eqStateFlow = MutableStateFlow<EqState>(initializeEqState())
        _presetsFlow = MutableStateFlow<List<EQPreset>>(initializePresets())
        _currentPresetFlow = MutableStateFlow<EQPreset>(initializeCurrentPreset())
        _bassBoostFlow = MutableStateFlow<EqEffectState<Float>>(initializeBassBoostState())
        _virtualizerFlow = MutableStateFlow<EqEffectState<Float>>(initializeVirtualizerState())
        _loudnessGainFlow = MutableStateFlow<EqEffectState<Float>>(initializeLoudnessGain())
        _presetReverbFlow = MutableStateFlow<EqEffectState<Int>>(initializePresetReverb())
    }

    suspend fun initializeEqualizer() {
        if (!isInitialized) {
            val result = runCatching { EffectSet(0) }
            if (result.isSuccess) {
                val temp = result.getOrThrow()

                setDefaultPresets(temp)
                numberOfBands = temp.getNumEqualizerBands().toInt()
                setBandLevelRange(temp.equalizer.getBandLevelRange())
                setCenterFreqs(temp)

                temp.release()
            }

            isInitialized = true
            initializeFlow()

            eqSession.update()
        }
    }

    suspend fun initializeFlow() {
        _eqStateFlow.emit(initializeEqState())
        _presetsFlow.emit(initializePresets())
        _currentPresetFlow.emit(initializeCurrentPreset())
        _bassBoostFlow.emit(initializeBassBoostState())
        _virtualizerFlow.emit(initializeVirtualizerState())
        _loudnessGainFlow.emit(initializeLoudnessGain())
        _presetReverbFlow.emit(initializePresetReverb())
    }

    fun openAudioEffectSession(audioSessionId: Int, internal: Boolean) {
        eqSession.openEqualizerSession(internal, audioSessionId)
    }

    fun closeAudioEffectSession(audioSessionId: Int, internal: Boolean) {
        eqSession.closeEqualizerSessions(internal, audioSessionId)
    }

    fun update() {
        eqSession.update()
    }

    fun release() {
        eqSession.release()
    }

    fun isPresetNameAvailable(presetName: String): Boolean {
        for ((name) in equalizerPresets) {
            if (name.equals(presetName, ignoreCase = true)) return false
        }
        return true
    }

    fun getNewExportName(): String = getFormattedFileName("BoomingEQ", "json")

    fun getNewPresetFromCustom(presetName: String): EQPreset {
        return EQPreset(getCustomPreset(), presetName, isCustom = false)
    }

    fun getEqualizerPresetsWithCustom(presets: List<EQPreset> = equalizerPresets) =
        presets.toMutableList().apply { add(getCustomPreset()) }

    fun renamePreset(preset: EQPreset, newName: String): Boolean {
        val trimmedName = newName.trim()
        if (trimmedName.isEmpty()) return false

        val currentPresets = equalizerPresets.toMutableList()
        if (currentPresets.any { it.name.equals(trimmedName, ignoreCase = true) }) {
            return false
        }

        val index = currentPresets.indexOfFirst { it.name == preset.name }
        if (index == -1) return false

        currentPresets[index] = preset.copy(name = trimmedName)

        setEqualizerPresets(currentPresets, updateFlow = true)
        if (preset == currentPreset) {
            setCurrentPreset(currentPresets[index])
        }
        return true
    }

    fun addPreset(preset: EQPreset, allowReplace: Boolean, usePreset: Boolean): Boolean {
        if (!preset.isValid) return false

        val currentPresets = equalizerPresets.toMutableList()
        val index = currentPresets.indexOfFirst { it.name.equals(preset.name, ignoreCase = true) }
        if (index != -1) {
            if (allowReplace) {
                currentPresets[index] = preset
                setEqualizerPresets(currentPresets, updateFlow = true)
                if (usePreset) {
                    setCurrentPreset(preset)
                }
                return true
            }
            return false
        }

        currentPresets.add(preset)
        setEqualizerPresets(currentPresets, updateFlow = true)
        if (usePreset) {
            setCurrentPreset(preset)
        }
        return true
    }

    fun removePreset(preset: EQPreset): Boolean {
        val currentPresets = equalizerPresets.toMutableList()
        val removed = currentPresets.removeIf { it.name == preset.name }
        if (!removed) return false

        setEqualizerPresets(currentPresets, updateFlow = true)
        if (preset == currentPreset) {
            setCurrentPreset(getCustomPreset())
        }
        return true
    }

    fun importPresets(toImport: List<EQPreset>): Int {
        if (toImport.isEmpty()) return 0

        val currentPresets = equalizerPresets.toMutableList()
        val numBands = numberOfBands

        var imported = 0
        for (preset in toImport) {
            if (!preset.isValid || preset.isCustom || preset.numberOfBands != numBands) {
                continue
            }
            val existingIndex = currentPresets.indexOfFirst { it.name.equals(preset.name, ignoreCase = true) }
            if (existingIndex >= 0) {
                currentPresets[existingIndex] = preset
                imported++
            } else {
                currentPresets.add(preset)
                imported++
            }
        }
        if (imported > 0) {
            setEqualizerPresets(currentPresets, updateFlow = true)
        }
        return imported
    }

    private fun setEqualizerPresets(presets: List<EQPreset>, updateFlow: Boolean) {
        mPreferences.edit { putString(Keys.PRESETS, Json.encodeToString(presets)) }
        if (updateFlow) {
            _presetsFlow.tryEmit(presets)
        }
    }

    @SuppressLint("KotlinPropertyAccess")
    fun setDefaultPresets(effectSet: EffectSet) {
        val presets = arrayListOf<EQPreset>()

        val numPresets = effectSet.numEqualizerPresets.toInt()
        val numBands = effectSet.numEqualizerBands.toInt()

        for (i in 0 until numPresets) {
            val name = effectSet.equalizer.getPresetName(i.toShort())

            val levels = IntArray(numBands)
            try {
                effectSet.equalizer.usePreset(i.toShort())
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }

            for (j in 0 until numBands) {
                levels[j] = effectSet.equalizer.getBandLevel(j.toShort()).toInt()
            }

            presets.add(EQPreset(name, levels, isCustom = false))
        }

        setEqualizerPresets(presets, false)
    }

    @Synchronized
    private fun getCustomPreset(): EQPreset {
        val json = mPreferences.getString(Keys.CUSTOM_PRESET, null).orEmpty().trim()
        return if (json.isEmpty()) {
            getAndSaveEmptyCustomPreset()
        } else runCatching {
            Json.decodeFromString<EQPreset>(json)
        }.getOrElse { null }?.takeIf { it.isValid } ?: getAndSaveEmptyCustomPreset()
    }

    @Synchronized
    private fun setCustomPreset(preset: EQPreset, usePreset: Boolean = true) {
        if (preset.isCustom) {
            if (usePreset) {
                setCurrentPreset(preset)
            }
            mPreferences.edit {
                putString(Keys.CUSTOM_PRESET, Json.encodeToString(preset))
            }
        }
    }

    private fun getAndSaveEmptyCustomPreset(): EQPreset {
        val emptyPreset = getEmptyPreset(CUSTOM_PRESET_NAME, true, numberOfBands)
        setCustomPreset(emptyPreset, usePreset = false)
        return emptyPreset
    }

    private fun getAndSaveDefaultOrEmptyPreset(): EQPreset {
        return equalizerPresets.firstOrNull()
            ?: getAndSaveEmptyCustomPreset()
    }

    @Synchronized
    private fun getCustomPresetFromCurrent(): EQPreset {
        return EQPreset(currentPreset, CUSTOM_PRESET_NAME, true)
    }

    /**
     * Copies the current preset to a "Custom" configuration
     * and sets the band level on it
     */
    fun setCustomPresetBandLevel(band: Int, level: Int) {
        var currentPreset = getCustomPresetFromCurrent()
        currentPreset.setBandLevel(band, level)
        setCustomPreset(currentPreset)
    }

    /**
     * Copies the current preset to a "Custom" configuration
     * and sets the effect value on it
     */
    private fun setCustomPresetEffect(effect: String, value: Float) {
        var currentPreset = getCustomPresetFromCurrent()
        if (value == 0f) { // zero means "disabled", we must remove disabled effects
            currentPreset.removeEffect(effect)
        } else {
            currentPreset.setEffect(effect, value)
        }
        setCustomPreset(currentPreset)
    }

    fun setCurrentPreset(eqPreset: EQPreset) {
        mPreferences.edit {
            putString(Keys.PRESET, Json.encodeToString(eqPreset))
        }
        _currentPresetFlow.tryEmit(eqPreset)
    }

    suspend fun setEqualizerState(update: EqUpdate<EqState>, apply: Boolean) {
        val newState = update.toState()
        if (apply) newState.apply()
        _eqStateFlow.emit(newState)
    }

    suspend fun setLoudnessGain(update: EqEffectUpdate<Float>, apply: Boolean) {
        val newState = update.toState()
        if (apply) newState.apply()
        _loudnessGainFlow.emit(newState)
    }

    suspend fun setPresetReverb(update: EqEffectUpdate<Int>, apply: Boolean) {
        val newState = update.toState()
        if (apply) newState.apply()
        _presetReverbFlow.tryEmit(newState)
    }

    suspend fun setBassBoost(update: EqEffectUpdate<Float>, apply: Boolean) {
        val newState = update.toState()
        if (apply) newState.apply()
        _bassBoostFlow.emit(newState)
    }

    suspend fun setVirtualizer(update: EqEffectUpdate<Float>, apply: Boolean) {
        val newState = update.toState()
        if (apply) newState.apply()
        _virtualizerFlow.emit(newState)
    }

    suspend fun applyPendingStates() {
        eqState.apply()
        loudnessGainState.apply()
        presetReverbState.apply()
        bassBoostState.apply()
        virtualizerState.apply()
    }

    private fun initializePresets(): List<EQPreset> {
        val json = mPreferences.getString(Keys.PRESETS, null).orEmpty()
        return runCatching {
            Json.decodeFromString<List<EQPreset>>(json).toMutableList()
        }.getOrElse {
            arrayListOf()
        }
    }

    private fun initializeCurrentPreset(): EQPreset {
        val json = mPreferences.getString(Keys.PRESET, null).orEmpty().trim()
        if (json.isEmpty()) {
            return getAndSaveDefaultOrEmptyPreset()
        }
        return runCatching {
            Json.decodeFromString<EQPreset>(json)
        }.getOrElse { getAndSaveDefaultOrEmptyPreset() }
    }

    private fun initializeEqState(): EqState {
        return EqState(
            isSupported = isEqualizerSupported,
            isEnabled = mPreferences.getBoolean(Keys.GLOBAL_ENABLED, false),
            onCommit = { state ->
                mPreferences.edit(commit = true) {
                    putBoolean(Keys.GLOBAL_ENABLED, state.isEnabled)
                }
            }
        )
    }

    private fun initializeLoudnessGain(): EqEffectState<Float> {
        return EqEffectState(
            isSupported = isLoudnessEnhancerSupported,
            isEnabled = mPreferences.getBoolean(Keys.LOUDNESS_ENABLED, false),
            value = mPreferences.getFloat(Keys.LOUDNESS_GAIN, OpenSLESConstants.MINIMUM_LOUDNESS_GAIN.toFloat()),
            onCommitEffect = { state ->
                mPreferences.edit(commit = true) {
                    putBoolean(Keys.LOUDNESS_ENABLED, state.isEnabled)
                    putFloat(Keys.LOUDNESS_GAIN, state.value)
                }
            }
        )
    }

    private fun initializePresetReverb(): EqEffectState<Int> {
        return EqEffectState(
            isSupported = isPresetReverbSupported,
            isEnabled = mPreferences.getBoolean(Keys.PRESET_REVERB_ENABLED, false),
            value = mPreferences.getInt(Keys.PRESET_REVERB_PRESET, 0),
            onCommitEffect = { state ->
                mPreferences.edit(commit = true) {
                    putBoolean(Keys.PRESET_REVERB_ENABLED, state.isEnabled)
                    putInt(Keys.PRESET_REVERB_PRESET, state.value)
                }
            }
        )
    }

    private fun initializeBassBoostState(): EqEffectState<Float> {
        return EqEffectState(
            isSupported = isBassBoostSupported,
            isEnabled = currentPreset.hasEffect(EFFECT_TYPE_BASS_BOOST),
            value = currentPreset.getEffect(EFFECT_TYPE_BASS_BOOST),
            onCommitEffect = { state ->
                setCustomPresetEffect(EFFECT_TYPE_BASS_BOOST, state.value)
            }
        )
    }

    private fun initializeVirtualizerState(): EqEffectState<Float> {
        return EqEffectState(
            isSupported = isVirtualizerSupported,
            isEnabled = currentPreset.hasEffect(EFFECT_TYPE_VIRTUALIZER),
            value = currentPreset.getEffect(EFFECT_TYPE_VIRTUALIZER),
            onCommitEffect = { state ->
                setCustomPresetEffect(EFFECT_TYPE_VIRTUALIZER, state.value)
            }
        )
    }

    var numberOfBands: Int
        get() = mPreferences.getInt(Keys.NUM_BANDS, 5)
        set(numberOfBands) = mPreferences.edit {
            putInt(Keys.NUM_BANDS, numberOfBands)
        }

    val bandLevelRange: IntArray
        get() {
            val bandLevelRange = mPreferences.getString(Keys.BAND_LEVEL_RANGE, null)
            if (bandLevelRange == null || bandLevelRange.trim().isEmpty()) {
                return intArrayOf(-1500, 1500)
            }
            val ranges = bandLevelRange.split(";").toTypedArray()
            val values = IntArray(ranges.size)
            for (i in values.indices) {
                values[i] = ranges[i].toInt()
            }
            return values
        }

    fun setBandLevelRange(bandLevelRange: ShortArray?) {
        if (bandLevelRange?.size == 2) {
            mPreferences.edit {
                putString(
                    Keys.BAND_LEVEL_RANGE,
                    String.format(Locale.ROOT, "%d;%d", bandLevelRange[0], bandLevelRange[1])
                )
            }
        }
    }

    val centerFreqs: IntArray
        get() = mPreferences.getString(Keys.CENTER_FREQUENCIES, EqualizerSession.getZeroedBandsString(numberOfBands))!!
            .split(DEFAULT_DELIMITER).toTypedArray().let { savedValue ->
                val frequencies = IntArray(savedValue.size)
                for (i in frequencies.indices) {
                    frequencies[i] = savedValue[i].toInt()
                }
                frequencies
            }

    fun setCenterFreqs(effectSet: EffectSet) {
        val numBands = numberOfBands
        val centerFreqs = StringBuilder()
        for (i in 0 until numBands) {
            centerFreqs.append(effectSet.equalizer.getCenterFreq(i.toShort()))
            if (i < numBands - 1) {
                centerFreqs.append(DEFAULT_DELIMITER)
            }
        }
        mPreferences.edit {
            putString(Keys.CENTER_FREQUENCIES, centerFreqs.toString())
        }
    }

    suspend fun resetConfiguration() {
        mPreferences.edit {
            putBoolean(Keys.IS_INITIALIZED, false)
            putBoolean(Keys.GLOBAL_ENABLED, false)
            putBoolean(Keys.LOUDNESS_ENABLED, false)
            putBoolean(Keys.PRESET_REVERB_ENABLED, false)
            remove(Keys.PRESETS)
            remove(Keys.PRESET)
            remove(Keys.CUSTOM_PRESET)
            remove(Keys.BAND_LEVEL_RANGE)
            remove(Keys.LOUDNESS_GAIN)
            remove(Keys.PRESET_REVERB_PRESET)
        }
        initializeEqualizer()
    }

    interface Keys {
        companion object {
            const val GLOBAL_ENABLED = "audiofx.global.enable"
            const val NUM_BANDS = "equalizer.number_of_bands"
            const val IS_INITIALIZED = "equalizer.initialized"
            const val LOUDNESS_ENABLED = "audiofx.eq.loudness.enable"
            const val LOUDNESS_GAIN = "audiofx.eq.loudness.gain"
            const val PRESET_REVERB_ENABLED = "audiofx.eq.presetreverb.enable"
            const val PRESET_REVERB_PRESET = "audiofx.eq.presetreverb.preset"
            const val PRESETS = "audiofx.eq.presets"
            const val PRESET = "audiofx.eq.preset"
            const val CUSTOM_PRESET = "audiofx.eq.preset.custom"
            const val BAND_LEVEL_RANGE = "equalizer.band_level_range"
            const val CENTER_FREQUENCIES = "equalizer.center_frequencies"
        }
    }

    companion object {

        const val EFFECT_TYPE_EQUALIZER = "0bed4300-ddd6-11db-8f34-0002a5d5c51b"
        const val EFFECT_TYPE_BASS_BOOST = "0634f220-ddd4-11db-a0fc-0002a5d5c51b"
        const val EFFECT_TYPE_VIRTUALIZER = "37cc2c00-dddd-11db-8577-0002a5d5c51b"
        const val EFFECT_TYPE_LOUDNESS_ENHANCER = "fe3199be-aed0-413f-87bb-11260eb63cf1"
        const val EFFECT_TYPE_PRESET_REVERB = "47382d60-ddd8-11db-bf3a-0002a5d5c51b"

        /**
         * Max number of EQ bands supported
         */
        const val EQUALIZER_MAX_BANDS = 6

        const val PREFERENCES_NAME = "BoomingAudioFX"
        private const val CUSTOM_PRESET_NAME = "Custom"
        private const val DEFAULT_DELIMITER = ";"
    }
}