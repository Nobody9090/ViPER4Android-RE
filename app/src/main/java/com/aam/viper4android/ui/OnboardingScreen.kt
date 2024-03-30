package com.aam.viper4android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    Column {
        Text(text = "Onboarding Screen")
        Button(onClick = onOnboardingComplete) {
            Text(text = "Complete onboarding")
        }
    }
}