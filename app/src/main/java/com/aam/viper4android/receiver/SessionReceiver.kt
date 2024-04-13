package com.aam.viper4android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.audiofx.AudioEffect
import android.util.Log
import androidx.core.content.ContextCompat
import com.aam.viper4android.driver.ViPERService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SessionReceiver"

@AndroidEntryPoint
class SessionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != AudioEffect.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION &&
            intent.action != AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION) return

        var packageName = intent.getStringExtra(AudioEffect.EXTRA_PACKAGE_NAME)
        val sessionId = intent.getIntExtra(AudioEffect.EXTRA_AUDIO_SESSION, -1)
        if (sessionId == -1) {
            Log.e(TAG, "onReceive: Missing sessionId!")
            return
        }

        if (packageName == null) {
            Log.w(TAG, "onReceive: Missing packageName for session $sessionId")
            packageName = "android" // Use a generic package name
        }

        Intent(context, ViPERService::class.java)
            .setAction(intent.action)
            .putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
            .putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
            .let {
                try {
                    ContextCompat.startForegroundService(context, it)
                } catch (e: Exception) {
                    Log.e(TAG, "onReceive: Failed to start service", e)
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
    }
}