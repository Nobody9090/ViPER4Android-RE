package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.driver.Preset
import com.aam.viper4android.driver.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViPERClarityViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.viperClarity.enabled
    val mode = viperManager.viperClarity.mode
    val gain = viperManager.viperClarity.gain

    fun setEnabled(enabled: Boolean) {
        viperManager.viperClarity.setEnabled(enabled)
    }

    fun setMode(mode: Int) {
        viperManager.viperClarity.setMode(mode)
    }

    fun resetMode() {
        viperManager.viperClarity.setMode(Preset.ViPERClarity.DEFAULT_MODE)
    }

    fun setGain(gain: Int) {
        viperManager.viperClarity.setGain(gain)
    }

    fun resetGain() {
        viperManager.viperClarity.setGain(Preset.ViPERClarity.DEFAULT_GAIN)
    }
}