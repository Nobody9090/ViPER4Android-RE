package com.aam.viper4android.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.driver.ViPERManager
import com.aam.viper4android.ui.model.StatusSession
import com.aam.viper4android.util.AndroidUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    @ApplicationContext context: Context,
    viperManager: ViPERManager,
) : ViewModel() {
    val sessions = viperManager.currentSessions.map { sessions ->
        sessions.map { session ->
            StatusSession(
                session = session,
                name = if (session.id != 0 && session.packageName == context.packageName) "Unknown" else AndroidUtils.getApplicationLabel(context, session.packageName),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    fun getVersionString(versionCode: UInt): String {
        return when (versionCode) {
            20240314u -> "0.7.0"
            else -> "Unknown"
        } + " ($versionCode)"
    }
}