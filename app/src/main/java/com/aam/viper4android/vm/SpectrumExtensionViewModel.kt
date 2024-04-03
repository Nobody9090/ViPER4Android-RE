package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpectrumExtensionViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.spectrumExtension.enabled
    val strength = viperManager.spectrumExtension.strength

    fun setEnabled(enabled: Boolean) {
        viperManager.spectrumExtension.setEnabled(enabled)
    }

    fun setStrength(strength: Int) {
        viperManager.spectrumExtension.setStrength(strength)
    }
}