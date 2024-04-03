package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
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

    fun setSoundField(soundField: Int) {
        viperManager.reverberation.setSoundField(soundField)
    }

    fun setDamping(damping: Int) {
        viperManager.reverberation.setDamping(damping)
    }

    fun setWetSignal(wetSignal: Int) {
        viperManager.reverberation.setWetSignal(wetSignal)
    }

    fun setDrySignal(drySignal: Int) {
        viperManager.reverberation.setDrySignal(drySignal)
    }
}