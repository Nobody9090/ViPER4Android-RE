package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.driver.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FIREqualizerViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.firEqualizer.enabled
    val gains = MutableStateFlow(listOf(
        10.0f, -1.0f, 8.0f, 6.0f, -9.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
        0f,
    ))

    fun setEnabled(enabled: Boolean) {
        viperManager.firEqualizer.setEnabled(enabled)
    }
}