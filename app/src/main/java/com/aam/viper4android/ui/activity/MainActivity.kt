package com.aam.viper4android.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.aam.viper4android.ui.ViPERApp
import com.aam.viper4android.ui.theme.ViPER4AndroidTheme
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViPER4AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ViPERApp()
                }
            }
        }

        requestNotificationPermissions()
        requestIgnoreBatteryOptimizations()
    }

    // Required for creating foreground services with the app in the background
    private fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(TAG, "requestNotificationPermissions() result called with: isGranted = $isGranted")
        }.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    // Required for creating foreground services with the app in the background
    @SuppressLint("BatteryLife")
    private fun requestIgnoreBatteryOptimizations() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isIgnoringBatteryOptimizations(packageName)) return

        registerForActivityResult(object : ActivityResultContract<Unit, Boolean>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
                    it.data = Uri.parse("package:$packageName")
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                return powerManager.isIgnoringBatteryOptimizations(packageName)
            }
        }) { isIgnoringBatteryOptimizations ->
            Log.d(TAG, "requestIgnoreBatteryOptimizations: Battery optimizations ignored: $isIgnoringBatteryOptimizations")
        }.launch(Unit)
    }
}
