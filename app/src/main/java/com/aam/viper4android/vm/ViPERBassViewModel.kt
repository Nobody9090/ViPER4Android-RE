package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.Preset
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ViPERBassViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.viperBass.enabled
    val mode = viperManager.viperBass.mode
    val frequency = viperManager.viperBass.frequency
    val gain = viperManager.viperBass.gain

    fun setEnabled(enabled: Boolean) {
        viperManager.viperBass.setEnabled(enabled)
    }
    fun setMode(mode: Int) {
        viperManager.viperBass.setMode(mode)
    }
    fun setFrequency(frequency: Int) {
        viperManager.viperBass.setFrequency(frequency)
    }
    fun setGain(gain: Int) {
        viperManager.viperBass.setGain(gain)
    }
}