package com.aam.viper4android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.ui.component.CategoryPreference
import com.aam.viper4android.ui.component.SwitchPreference
import com.aam.viper4android.ui.component.TextPreference
import com.aam.viper4android.vm.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        modifier = Modifier.padding(start = 8.dp), // Hack, will not match figma values
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        val legacyMode = viewModel.legacyMode.collectAsState().value

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CategoryPreference(
                title = "Processing"
            ) {
                SwitchPreference(
                    title = "Legacy mode",
                    summary = "Attaches to system-wide session \"0\" (deprecated).",
                    checked = legacyMode,
                    onCheckedChange = viewModel::setLegacyMode
                )
            }
            CategoryPreference(
                title = "Background limits"
            ) {
                SwitchPreference(
                    title = "Keep service always running",
                    summary = "Avoids foreground service start issues on some devices.",
                    checked = false,
                    onCheckedChange = { /*TODO*/ }
                )
            }
            CategoryPreference(
                title = "About"
            ) {
                TextPreference(
                    title = "Driver reverse engineering",
                    summary = "Martmists-GH, iscle, xddxdd"
                )
                TextPreference(
                    title = "App programming",
                    summary = "iscle"
                )
                TextPreference(
                    title = "App design",
                    summary = "WSTxda"
                )
                TextPreference(
                    title = "Special thanks",
                    summary = "pittvandewitt, ViPER ACOUSTIC"
                )
            }
        }
    }
}