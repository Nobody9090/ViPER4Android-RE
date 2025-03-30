package com.aam.viper4android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.aam.viper4android.SettingsRepository
import com.aam.viper4android.driver.ViPERService
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "BootCompletedReceiver"

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {
    lateinit var settingsRepository: SettingsRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        if (!settingsRepository.serviceAlwaysActive) return

        Intent(context, ViPERService::class.java).let {
            try {
                ContextCompat.startForegroundService(context, it)
            } catch (e: Exception) {
                Log.e(TAG, "onReceive: Failed to start service", e)
            }
        }
    }
}