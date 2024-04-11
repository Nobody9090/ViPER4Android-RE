package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.driver.Preset
import com.aam.viper4android.driver.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DifferentialSurroundViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.differentialSurround.enabled
    val delay = viperManager.differentialSurround.delay

    fun setEnabled(enabled: Boolean) {
        viperManager.differentialSurround.setEnabled(enabled)
    }

    fun setDelay(delay: Int) {
        viperManager.differentialSurround.setDelay(delay)
    }

    fun resetDelay() {
        viperManager.differentialSurround.setDelay(Preset.DifferentialSurround.DEFAULT_DELAY)
    }
}