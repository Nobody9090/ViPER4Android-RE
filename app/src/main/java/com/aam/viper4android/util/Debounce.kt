package com.aam.viper4android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// A utility function to debounce any function call
fun CoroutineScope.debounce(
    delayMillis: Long, // Delay in ms
    action: suspend () -> Unit // The action to debounce
): () -> Unit {
    var debounceJob: Job? = null
    return {
        debounceJob?.cancel() // Cancel the previous job if it's still active
        debounceJob = launch {
            delay(delayMillis) // Wait for the delay
            action() // Execute the action
        }
    }
}
