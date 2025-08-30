package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.driver.Preset
import com.aam.viper4android.driver.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FieldSurroundViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.fieldSurround.enabled
    val surroundStrength = viperManager.fieldSurround.surroundStrength
    val midImageStrength = viperManager.fieldSurround.midImageStrength

    fun setEnabled(enabled: Boolean) {
        viperManager.fieldSurround.setEnabled(enabled)
    }

    fun setSurroundStrength(surroundStrength: Int) {
        viperManager.fieldSurround.setSurroundStrength(surroundStrength)
    }

    fun resetSurroundStrength() {
        viperManager.fieldSurround.setSurroundStrength(Preset.FieldSurround.DEFAULT_SURROUND_STRENGTH)
    }

    fun setMidImageStrength(midImageStrength: Int) {
        viperManager.fieldSurround.setMidImageStrength(midImageStrength)
    }

    fun resetMidImageStrength() {
        viperManager.fieldSurround.setMidImageStrength(Preset.FieldSurround.DEFAULT_MID_IMAGE_STRENGTH)
    }
}