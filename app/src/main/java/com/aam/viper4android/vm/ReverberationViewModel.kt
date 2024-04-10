package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.Preset
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReverberationViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.reverberation.enabled
    val roomSize = viperManager.reverberation.roomSize
    val soundField = viperManager.reverberation.soundField
    val damping = viperManager.reverberation.damping
    val wetSignal = viperManager.reverberation.wetSignal
    val drySignal = viperManager.reverberation.drySignal

    fun setEnabled(enabled: Boolean) {
        viperManager.reverberation.setEnabled(enabled)
    }

    fun setRoomSize(roomSize: Int) {
        viperManager.reverberation.setRoomSize(roomSize)
    }

    fun resetRoomSize() {
        viperManager.reverberation.setRoomSize(Preset.Reverberation.DEFAULT_ROOM_SIZE)
    }

    fun setSoundField(soundField: Int) {
        viperManager.reverberation.setSoundField(soundField)
    }

    fun resetSoundField() {
        viperManager.reverberation.setSoundField(Preset.Reverberation.DEFAULT_SOUND_FIELD)
    }

    fun setDamping(damping: Int) {
        viperManager.reverberation.setDamping(damping)
    }

    fun resetDamping() {
        viperManager.reverberation.setDamping(Preset.Reverberation.DEFAULT_DAMPING)
    }

    fun setWetSignal(wetSignal: Int) {
        viperManager.reverberation.setWetSignal(wetSignal)
    }

    fun resetWetSignal() {
        viperManager.reverberation.setWetSignal(Preset.Reverberation.DEFAULT_WET_SIGNAL)
    }

    fun setDrySignal(drySignal: Int) {
        viperManager.reverberation.setDrySignal(drySignal)
    }

    fun resetDrySignal() {
        viperManager.reverberation.setDrySignal(Preset.Reverberation.DEFAULT_DRY_SIGNAL)
    }
}