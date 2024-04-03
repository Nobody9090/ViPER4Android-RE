package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TubeSimulator6N1JViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.tubeSimulator6N1J.enabled

    fun setEnabled(enabled: Boolean) {
        viperManager.tubeSimulator6N1J.setEnabled(enabled)
    }
}