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

private val outputGainValues = listOf(
    1,
    5,
    10,
    20,
    30,
    40,
    50,
    60,
    70,
    80,
    90,
    100,
    110,
    120,
    130,
    140,
    150,
    160,
    170,
    180,
    190,
    200,
)

private val thresholdLimitValues = listOf(
    30,
    50,
    70,
    80,
    90,
    100,
)

@HiltViewModel
class MasterLimiterViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val outputGain = viperManager.masterLimiter.outputGain
        .map(outputGainValues::indexOf)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = outputGainValues.indexOf(Preset.MasterLimiter.DEFAULT_OUTPUT_GAIN)
        )
    val outputPan = viperManager.masterLimiter.outputPan
    val thresholdLimit = viperManager.masterLimiter.thresholdLimit
        .map(thresholdLimitValues::indexOf)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = thresholdLimitValues.indexOf(Preset.MasterLimiter.DEFAULT_THRESHOLD_LIMIT)
        )

    fun setOutputGain(outputGain: Int) {
        viperManager.masterLimiter.setOutputGain(outputGainValues[outputGain])
    }

    fun resetOutputGain() {
        viperManager.masterLimiter.setOutputGain(Preset.MasterLimiter.DEFAULT_OUTPUT_GAIN)
    }

    fun setOutputPan(outputPan: Int) {
        viperManager.masterLimiter.setOutputPan(outputPan)
    }

    fun resetOutputPan() {
        viperManager.masterLimiter.setOutputPan(Preset.MasterLimiter.DEFAULT_OUTPUT_PAN)
    }

    fun setThresholdLimit(thresholdLimit: Int) {
        viperManager.masterLimiter.setThresholdLimit(thresholdLimitValues[thresholdLimit])
    }

    fun resetThresholdLimit() {
        viperManager.masterLimiter.setThresholdLimit(Preset.MasterLimiter.DEFAULT_THRESHOLD_LIMIT)
    }
}