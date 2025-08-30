package com.aam.viper4android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aam.viper4android.ui.ViPERApp
import com.aam.viper4android.ui.theme.ViPER4AndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViPER4AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    ViPERApp()
                }
            }
        }
    }
}