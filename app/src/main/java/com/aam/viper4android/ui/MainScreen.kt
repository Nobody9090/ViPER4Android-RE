package com.aam.viper4android.ui

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.PresetDialog
import com.aam.viper4android.R
import com.aam.viper4android.RenamePresetDialog
import com.aam.viper4android.StatusDialog
import com.aam.viper4android.ViPERService
import com.aam.viper4android.ui.component.SwitchBar
import com.aam.viper4android.ui.effect.AnalogXEffect
import com.aam.viper4android.ui.effect.AuditorySystemProtectionEffect
import com.aam.viper4android.ui.effect.ConvolverEffect
import com.aam.viper4android.ui.effect.DifferentialSurroundEffect
import com.aam.viper4android.ui.effect.DynamicSystemEffect
import com.aam.viper4android.ui.effect.FETCompressorEffect
import com.aam.viper4android.ui.effect.FIREqualizerEffect
import com.aam.viper4android.ui.effect.FieldSurroundEffect
import com.aam.viper4android.ui.effect.HeadphoneSurroundPlusEffect
import com.aam.viper4android.ui.effect.MasterLimiterEffect
import com.aam.viper4android.ui.effect.PlaybackGainControlEffect
import com.aam.viper4android.ui.effect.ReverberationEffect
import com.aam.viper4android.ui.effect.SpeakerOptimizationEffect
import com.aam.viper4android.ui.effect.SpectrumExtensionEffect
import com.aam.viper4android.ui.effect.TubeSimulator6N1JEffect
import com.aam.viper4android.ui.effect.ViPERBassEffect
import com.aam.viper4android.ui.effect.ViPERClarityEffect
import com.aam.viper4android.ui.effect.ViPERDDCEffect
import com.aam.viper4android.vm.MainViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics

private const val TAG = "MainScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
) {
    val context = LocalContext.current
    val presetName = viewModel.presetName.collectAsState().value
    val enabled = viewModel.enabled.collectAsState().value

    LaunchedEffect(context) {
        Intent(context, ViPERService::class.java).let {
            try {
                ContextCompat.startForegroundService(context, it)
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: Failed to start service", e)
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    var openRenamePresetDialog by rememberSaveable { mutableStateOf(false) }
    var openStatusDialog by rememberSaveable { mutableStateOf(false) }
    var openPresetDialog by rememberSaveable { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        modifier = Modifier.padding(start = 8.dp), // Hack, will not match figma values
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(onClick = { openPresetDialog = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_presets),
                                contentDescription = "Presets"
                            )
                        }
                        IconButton(onClick = { openStatusDialog = true }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_driver),
                                contentDescription = "Driver status"
                            )
                        }
                        IconButton(onClick = onNavigateToSettings) {
                            Icon(
                                painter = painterResource(R.drawable.ic_settings),
                                contentDescription = "Settings"
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
//        BottomSheet(
//            onDismissRequest = { /*TODO*/ }
//        ) {
//            Text("Hello, Bottom Sheet!")
//        }
//        EqualizerBottomSheetPreview()

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(29.dp))
            SwitchBar(
                modifier = Modifier.padding(horizontal = 24.dp),
                title = {
                    Crossfade(
                        targetState = enabled,
                    ) { enabled ->
                        Text(
                            text = if (enabled) {
                                "Enabled"
                            } else {
                                "Disabled"
                            }
                        )
                    }
                },
                checked = enabled,
                onCheckedChange = viewModel::setEnabled
            )

            AnimatedVisibility(visible = enabled) {
                Column {
                    Spacer(Modifier.height(29.dp))

                    MasterLimiterEffect()
                    PlaybackGainControlEffect()
                    FETCompressorEffect()
                    ViPERDDCEffect()
                    SpectrumExtensionEffect()
                    FIREqualizerEffect()
                    ConvolverEffect()
                    FieldSurroundEffect()
                    DifferentialSurroundEffect()
                    HeadphoneSurroundPlusEffect()
                    ReverberationEffect()
                    DynamicSystemEffect()
                    TubeSimulator6N1JEffect()
                    ViPERBassEffect()
                    ViPERClarityEffect()
                    AuditorySystemProtectionEffect()
                    AnalogXEffect()
                    SpeakerOptimizationEffect()
                }
            }
        }
    }

    if (openRenamePresetDialog) {
        RenamePresetDialog(
            name = presetName,
            onNameChanged = viewModel::setPresetName,
            onDismissRequest = { openRenamePresetDialog = false }
        )
    }
    if (openStatusDialog) {
        StatusDialog(onDismissRequest = { openStatusDialog = false })
    }
    if (openPresetDialog) {
        PresetDialog(onDismissRequest = { openPresetDialog = false })
    }
}
