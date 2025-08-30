package com.aam.viper4android.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.Instant

@Composable
fun ElapsedTimeSince(
    startInstant: Instant,
    content: @Composable (String) -> Unit,
) {
    var elapsedTime by remember { mutableStateOf(Duration.ZERO) }

    // Update the elapsed time every second
    LaunchedEffect(startInstant) {
        while (true) {
            elapsedTime = Duration.between(startInstant, Instant.now())
            delay(1000) // Update every second
        }
    }

    content(formatDuration(elapsedTime))
}

fun formatDuration(duration: Duration): String {
    return when {
        duration.toMinutes() < 1 -> "${duration.seconds}s"
        duration.toHours() < 1 -> "${duration.toMinutes()}m ${duration.seconds % 60}s"
        duration.toDays() < 1 -> "${duration.toHours()}h ${duration.toMinutes() % 60}m"
        else -> "${duration.toDays()}d ${duration.toHours() % 24}h"
    }
}