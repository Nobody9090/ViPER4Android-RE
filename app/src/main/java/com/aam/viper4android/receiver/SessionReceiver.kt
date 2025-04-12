package com.aam.viper4android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import androidx.core.content.ContextCompat
import com.aam.viper4android.driver.ViPEREffect
import com.aam.viper4android.driver.ViPERService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SessionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION &&
            intent.action != AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION) return
        if (!ViPEREffect.isAvailable) return

        val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
        if (sessionId == -1) {
            Timber.e("onReceive: Missing sessionId!")
            return
        }

        var packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
        if (packageName == null) {
            Timber.w("onReceive: Missing packageName for session $sessionId")
            packageName = context.packageName // Use a generic package name
        }

        Intent(context, ViPERService::class.java)
            .setAction(intent.action)
            .putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
            .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
            .let {
                try {
                    ContextCompat.startForegroundService(context, it)
                } catch (e: Exception) {
                    Timber.e(e, "onReceive: Failed to start service")
                }
            }
    }
}