package com.aam.viper4android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.aam.viper4android.SettingsRepository
import com.aam.viper4android.driver.ViPEREffect
import com.aam.viper4android.driver.ViPERService
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class BootCompletedReceiver : BroadcastReceiver() {
    lateinit var settingsRepository: SettingsRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        if (!ViPEREffect.isAvailable || !settingsRepository.serviceAlwaysActive) return

        Intent(context, ViPERService::class.java).let {
            try {
                ContextCompat.startForegroundService(context, it)
            } catch (e: Exception) {
                Timber.e(e, "onReceive: Failed to start service")
            }
        }
    }
}