package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.SettingsRepository
import com.aam.viper4android.persistence.ViPERSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val viperSettings: ViPERSettings,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {
    val legacyMode = viperSettings.legacyMode

    fun setLegacyMode(legacyMode: Boolean) {
        viperSettings.setLegacyMode(legacyMode)
    }
}