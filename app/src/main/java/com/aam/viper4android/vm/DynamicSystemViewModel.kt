package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.Preset
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DynamicSystemViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.dynamicSystem.enabled
    val deviceType = viperManager.dynamicSystem.deviceType
    val dynamicBassStrength = viperManager.dynamicSystem.dynamicBassStrength

    fun setEnabled(enabled: Boolean) {
        viperManager.dynamicSystem.setEnabled(enabled)
    }

    fun setDeviceType(deviceType: Preset.DynamicSystem.DeviceType) {
        viperManager.dynamicSystem.setDeviceType(deviceType)
    }

    fun setDynamicBassStrength(dynamicBassStrength: Int) {
        viperManager.dynamicSystem.setDynamicBassStrength(dynamicBassStrength)
    }

    fun resetDynamicBassStrength() {
        viperManager.dynamicSystem.setDynamicBassStrength(Preset.DynamicSystem.DEFAULT_DYNAMIC_BASS_STRENGTH)
    }
}