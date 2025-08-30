package com.aam.viper4android.vm

import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.driver.ViPERManager
import com.aam.viper4android.driver.ViPERService
import com.aam.viper4android.ui.model.StatusSession
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    viperManager: ViPERManager,
) : ViewModel() {
    val sessions = viperManager.currentSessions.map { sessions ->
        sessions.map { session ->
            StatusSession(
                session = session,
                name = session.getApplicationLabel(context),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5000L
        ),
        initialValue = emptyList(),
    )

    fun closeSession(session: StatusSession) {
        Intent(context, ViPERService::class.java)
            .setAction(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
            .putExtra(AudioEffect.EXTRA_AUDIO_SESSION, session.id)
            .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, session.packageName)
            .let {
                try {
                    ContextCompat.startForegroundService(context, it)
                } catch (e: Exception) {
                    Timber.e(e, "closeSession: Failed to start service")
                }
            }
    }
}