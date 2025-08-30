package com.aam.viper4android.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.ktx.plus
import com.aam.viper4android.ui.component.CategoryPreference
import com.aam.viper4android.ui.component.SwitchPreference
import com.aam.viper4android.ui.component.TextPreference
import com.aam.viper4android.vm.SettingsViewModel
import androidx.core.net.toUri
import com.aam.viper4android.R

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
                        text = stringResource(R.string.settings),
                        modifier = Modifier.padding(start = 8.dp), // Hack, will not match figma values
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding + PaddingValues(vertical = 8.dp),
        ) {
            item {
                CategoryPreference(
                    title = stringResource(R.string.processing)
                ) {
                    val legacyMode by viewModel.legacyMode.collectAsStateWithLifecycle()
                    SwitchPreference(
                        title = stringResource(R.string.legacy_mode),
                        summary = stringResource(R.string.settings_legacy_mode_description),
                        checked = legacyMode,
                        onCheckedChange = viewModel::setLegacyMode
                    )
                }
            }

            item {
                CategoryPreference(
                    title = stringResource(R.string.background_limits)
                ) {
                    SwitchPreference(
                        title = stringResource(R.string.keep_service_always_running),
                        summary = stringResource(R.string.settings_keep_service_always_running_description),
                        checked = false,
                        onCheckedChange = { /*TODO*/ }
                    )
                }
            }

            item {
                CategoryPreference(
                    title = stringResource(R.string.about)
                ) {
                    // All names are alphabetically sorted
                    TextPreference(
                        title = stringResource(R.string.app_design),
                        summary = "WSTxda"
                    )
                    TextPreference(
                        title = stringResource(R.string.app_programming),
                        summary = "iscle"
                    )
                    TextPreference(
                        title = stringResource(R.string.driver_reverse_engineering),
                        summary = "iscle, Martmists-GH, xddxdd"
                    )
                    TextPreference(
                        title = stringResource(R.string.special_thanks),
                        summary = "pittvandewitt, ViPER ACOUSTIC"
                    )

                    val context = LocalContext.current
                    TextPreference(
                        modifier = Modifier.clickable {
                            try {
                                val url = "https://github.com/AndroidAudioMods/ViPER4Android"
                                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Ignored
                            }
                        },
                        title = stringResource(R.string.source_code),
                        summary = "github.com/AndroidAudioMods/ViPER4Android"
                    )
                }
            }
        }
    }
}