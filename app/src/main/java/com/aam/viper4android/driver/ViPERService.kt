package com.aam.viper4android.driver

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.media.audiofx.AudioEffect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.aam.viper4android.R
import com.aam.viper4android.ViPERApplication
import com.aam.viper4android.persistence.ViPERSettings
import com.aam.viper4android.util.AndroidUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ViPERService : LifecycleService() {
    @Inject lateinit var viperManager: ViPERManager
    @Inject lateinit var viperSettings: ViPERSettings

    private val intentsFlow = MutableSharedFlow<Intent>(
        extraBufferCapacity = Int.MAX_VALUE,
    )

    private var foreground = false
    private var lastStartId = -1;

    override fun onCreate() {
        super.onCreate()
        collectFlows()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (!foreground) {
            try {
                ServiceCompat.startForeground(
                    this,
                    SERVICE_NOTIFICATION_ID,
                    getNotification(),
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
                )
                Timber.d("onStartCommand: Started foreground service")
                foreground = true
            } catch (e: Exception) {
                Timber.e(e, "onStartCommand: Failed to start foreground service")
            }
        }

        if (intent != null) {
            intent.putExtra(EXTRA_START_ID, startId)
            intentsFlow.tryEmit(intent)
        }

        return START_STICKY
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            intentsFlow.collect(::handleIntent)
        }

        lifecycleScope.launch {
            combine(
                viperManager.currentRoute,
                viperSettings.legacyMode
            ) { currentRoute, legacyMode ->
                updateNotification()
            }
        }

        lifecycleScope.launch {
            viperManager.waitForReady()
            viperManager.currentSessions.collect { sessions ->
                // todo: stop service when started without an intent that modifies the sessions
                if (sessions.isEmpty()) {
                    val startId = lastStartId
                    if (startId != -1) {
                        Timber.d("collectSessions: No active sessions, stopping service")
                        stopSelf(startId)
                    }
                } else {
                    updateNotification()
                }
            }
        }
    }

    private suspend fun handleIntent(intent: Intent) {
        val startId = intent.getIntExtra(EXTRA_START_ID, -1)
        if (startId != -1) lastStartId = startId

        when (intent.action) {
            AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION -> {
                val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
                val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
                if (sessionId == -1 || packageName == null) return
                viperManager.addSession(packageName, sessionId)
            }
            AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION -> {
                val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
                val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
                if (sessionId == -1 || packageName == null) return
                viperManager.removeSession(packageName, sessionId)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateNotification() {
        if (!foreground) return
        NotificationManagerCompat.from(this)
            .notify(SERVICE_NOTIFICATION_ID, getNotification())
    }

    private fun getNotification(): Notification {
        val pendingIntent = packageManager.getLaunchIntentForPackage(packageName).let {
            PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }

        val title = getString(
            R.string.device_connected,
            viperManager.currentRoute.value.getName()
        )

        val text = if (viperSettings.legacyMode.value)
            getString(R.string.legacy_mode)
        else
            getSessionAppLabelsString()

        return NotificationCompat.Builder(this, ViPERApplication.SERVICES_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setColor(Color.parseColor("#6100ED"))
            .setOngoing(true)
            .setShowWhen(false)
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun getSessionAppLabelsString(): String {
        val sessions = viperManager.currentSessions.value
        return sessions.distinctBy { it.packageName }.map {
            if (it.id != 0 && it.packageName == packageName) "Unknown" else AndroidUtils.getApplicationLabel(this, it.packageName)
        }.distinct().joinToString().ifEmpty { getString(R.string.no_active_sessions) }
    }

    companion object {
        private const val SERVICE_NOTIFICATION_ID = 1
        private const val EXTRA_START_ID = "startId"
    }
}