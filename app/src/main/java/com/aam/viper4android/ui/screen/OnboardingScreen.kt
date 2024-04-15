package com.aam.viper4android.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.aam.viper4android.R
import com.aam.viper4android.driver.ViPERManager
import com.aam.viper4android.ui.component.OnboardingCard

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    val context = LocalContext.current
    val powerManager = remember(context) { context.getSystemService(PowerManager::class.java) }
    var isIgnoringBatteryOptimizations by remember(context, powerManager) { mutableStateOf(powerManager.isIgnoringBatteryOptimizations(context.packageName)) }
    var hasNotificationPermission by remember(context) { mutableStateOf(
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    ) }

    val batteryOptimizationsResultLauncher = rememberLauncherForActivityResult(
        object : ActivityResultContract<Unit, Boolean>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
                    it.data = Uri.parse("package:${context.packageName}")
                }
            }
            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                return powerManager.isIgnoringBatteryOptimizations(context.packageName)
            }

            override fun getSynchronousResult(
                context: Context,
                input: Unit
            ): SynchronousResult<Boolean> {
                return SynchronousResult(powerManager.isIgnoringBatteryOptimizations(context.packageName))
            }
        }
    ) {
        isIgnoringBatteryOptimizations = it
    }

    val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        hasNotificationPermission = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp,
                top = 48.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_viper),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Welcome to ViPER4Android",
            fontSize = 45.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 45.sp
        )

        Text(
            text = "To proceed with the application, you need to install the driver and grant some permissions for the application to work correctly.",
            style = MaterialTheme.typography.bodyLarge,
        )

        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OnboardingCard(
                isDone = ViPERManager.isViperAvailable,
                title = "Driver installed",
                description = "The driver installed on this device",
                onClick = {  }
            )

            OnboardingCard(
                isDone = isIgnoringBatteryOptimizations,
                title = "Ignore battery optimization",
                description = "Allows you to follow the sound process with greater precision",
                onClick = { batteryOptimizationsResultLauncher.launch(Unit) }
            )

            OnboardingCard(
                isDone = hasNotificationPermission,
                title = "Notification access",
                description = "Allows the application to display notifications",
                onClick = { notificationPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
            )

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Button(
                    onClick = onOnboardingComplete,
                    enabled = ViPERManager.isViperAvailable && isIgnoringBatteryOptimizations && hasNotificationPermission
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowForward,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = "Finish")
                }
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    Surface {
        OnboardingScreen(
            onOnboardingComplete = {}
        )
    }
}