package com.aam.viper4android.vm

import androidx.lifecycle.ViewModel
import com.aam.viper4android.driver.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuditorySystemProtectionViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    val enabled = viperManager.auditorySystemProtection.enabled

    fun setEnabled(enabled: Boolean) {
        viperManager.auditorySystemProtection.setEnabled(enabled)
    }
}