package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.Preset
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FieldSurroundViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.fieldSurroundEffect.enabled
    val surroundStrength = viperManager.fieldSurroundEffect.surroundStrength
    val midImageStrength = viperManager.fieldSurroundEffect.midImageStrength

    fun setEnabled(enabled: Boolean) {
        viperManager.fieldSurroundEffect.setEnabled(enabled)
    }

    fun setSurroundStrength(surroundStrength: Int) {
        viperManager.fieldSurroundEffect.setSurroundStrength(surroundStrength)
    }

    fun resetSurroundStrength() {
        viperManager.fieldSurroundEffect.setSurroundStrength(Preset.FieldSurround.DEFAULT_SURROUND_STRENGTH)
    }

    fun setMidImageStrength(midImageStrength: Int) {
        viperManager.fieldSurroundEffect.setMidImageStrength(midImageStrength)
    }

    fun resetMidImageStrength() {
        viperManager.fieldSurroundEffect.setMidImageStrength(Preset.FieldSurround.DEFAULT_MID_IMAGE_STRENGTH)
    }
}