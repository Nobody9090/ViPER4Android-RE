package com.aam.viper4android.driver

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
import com.aam.viper4android.ktx.getBootCount
import com.aam.viper4android.persistence.SessionDao
import com.aam.viper4android.persistence.ViPERSettings
import com.aam.viper4android.persistence.model.PersistedSession
import com.aam.viper4android.util.AndroidUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ViPERService : LifecycleService() {
    @Inject lateinit var viperManager: ViPERManager
    @Inject lateinit var viperSettings: ViPERSettings
    @Inject lateinit var sessionDao: SessionDao

    private val bootCount: Int = getBootCount(contentResolver)

    private var sessionMutex = Mutex(locked = true)

    override fun onCreate() {
        super.onCreate()
        deleteObsoleteSessions()
        restoreSessions()
        collectFlows()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        try {
            ServiceCompat.startForeground(
                this,
                SERVICE_NOTIFICATION_ID,
                getNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
            Timber.d("onStartCommand: Started foreground service")
        } catch (e: Exception) {
            Timber.e(e, "onStartCommand: Failed to start foreground service")
        }

        when (intent?.action) {
            AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION -> {
                val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
                val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
                addSession(sessionId, packageName, startId)
            }
            AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION -> {
                val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
                val packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
                removeSession(sessionId, packageName, startId)
            }
            else -> {
                checkSessions(startId)
            }
        }

        return START_STICKY
    }

    private fun deleteObsoleteSessions() {
        lifecycleScope.launch(Dispatchers.IO) {
            sessionDao.deleteObsolete(bootCount)
        }
    }

    private fun restoreSessions() {
        lifecycleScope.launch {
            if (viperSettings.legacyMode.value) {
                Timber.d("restoreSessions: Legacy mode is enabled, not restoring sessions")
                viperManager.addSession(0, packageName)
            } else {
                Timber.d("restoreSessions: Restoring sessions from database")
                withContext(Dispatchers.IO) { sessionDao.getAll(bootCount) }.forEach { session ->
                    Timber.d("restoreSessions: Restoring session ${session.id} for package ${session.packageName}")
                    viperManager.addSession(session.id, session.packageName)
                }
            }
            sessionMutex.unlock()
        }
    }

    private fun addSession(id: Int, packageName: String?, startId: Int) {
        lifecycleScope.launch {
            sessionMutex.withLock {
                if (id != -1) {
                    withContext(Dispatchers.IO) {
                        sessionDao.insert(PersistedSession(
                            id = id,
                            packageName = packageName,
                            bootCount = bootCount
                        ))
                    }
                    if (!viperSettings.legacyMode.value) {
                        Timber.d("addSession: Adding session $id for package $packageName")
                        viperManager.addSession(id, packageName)
                    } else {
                        Timber.d("addSession: Legacy mode is enabled, not adding session $id for package $packageName")
                    }
                }

                if (!viperManager.hasSessions()) {
                    Timber.d("addSession: No sessions are active, stopping service (startId: $startId)")
                    stopSelf(startId)
                }
            }
        }
    }

    private fun removeSession(id: Int, packageName: String?, startId: Int) {
        lifecycleScope.launch {
            sessionMutex.withLock {
                if (id != -1) {
                    withContext(Dispatchers.IO) {
                        sessionDao.delete(id)
                    }
                    viperManager.removeSession(id, packageName)
                }

                if (!viperManager.hasSessions()) {
                    Timber.d("removeSession: No sessions are active, stopping service (startId: $startId)")
                    stopSelf(startId)
                }
            }
        }
    }

    private fun checkSessions(startId: Int) {
        lifecycleScope.launch {
            sessionMutex.withLock {
                if (!viperManager.hasSessions()) {
                    Timber.d("checkSessions: No sessions are active, stopping service (startId: $startId)")
                    stopSelf(startId)
                }
            }
        }
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            viperSettings.legacyMode.collect { legacyMode ->
                // TODO: Handle legacy mode changes
            }
        }

        lifecycleScope.launch {
            combine(
                viperManager.currentRoute,
                viperSettings.legacyMode,
                viperManager.currentSessions
            ) { currentRoute, legacyMode, sessions ->
                updateNotification()
            }
        }
    }

    private fun updateNotification() {
        try {
            NotificationManagerCompat.from(this)
                .notify(SERVICE_NOTIFICATION_ID, getNotification())
        } catch (e: Exception) {
            Timber.e(e, "updateNotification: Failed to update notification")
        }
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
        return sessions.map {
            getSessionAppLabelString(it.packageName)
        }.distinct().joinToString().ifEmpty { getString(R.string.no_active_sessions) }
    }

    private fun getSessionAppLabelString(packageName: String?): String {
        return if (packageName == null) {
            "Unknown"
        } else {
            AndroidUtils.getApplicationLabel(this, packageName)
        }
    }

    companion object {
        private const val SERVICE_NOTIFICATION_ID = 1
    }
}