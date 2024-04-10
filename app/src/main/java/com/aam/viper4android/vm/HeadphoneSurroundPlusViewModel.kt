package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.Preset
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeadphoneSurroundPlusViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.headphoneSurroundPlus.enabled
    val level = viperManager.headphoneSurroundPlus.level

    fun setEnabled(enabled: Boolean) {
        viperManager.headphoneSurroundPlus.setEnabled(enabled)
    }

    fun setLevel(level: Int) {
        viperManager.headphoneSurroundPlus.setLevel(level)
    }

    fun resetLevel() {
        viperManager.headphoneSurroundPlus.setLevel(Preset.HeadphoneSurroundPlus.DEFAULT_LEVEL)
    }
}