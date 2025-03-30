package com.aam.viper4android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.driver.Architecture
import com.aam.viper4android.driver.DisableReason
import com.aam.viper4android.vm.StatusViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun StatusDialog(
    statusViewModel: StatusViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.status)) },
        text = {
            val timerState = remember { mutableIntStateOf(0) }

            LaunchedEffect(timerState) {
                while (isActive) {
                    delay(1000)
                    timerState.intValue++
                }
            }

            val sessions = statusViewModel.sessions.collectAsState().value
            if (sessions.isEmpty()) {
                Text(text = stringResource(id = R.string.no_active_sessions))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(sessions) { session ->
                        val status by remember {
                            derivedStateOf {
                                session.session.effect.status
                            }
                        }

                        key(session.session.id) {
                            Column {
                                Text(
                                    text = session.name,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                                Text(text = stringResource(
                                    R.string.version,
                                    statusViewModel.getVersionString(status.getVersion())
                                ))
                                Text(text = stringResource(
                                    R.string.architecture,
                                    Architecture.fromValue(status.getArchitecture())
                                ))
                                Text(text = stringResource(
                                    R.string.enabled,
                                    if (status.getEnabled())
                                        stringResource(R.string.yes)
                                    else
                                        stringResource(R.string.no)
                                ))
                                Text(text = stringResource(
                                    R.string.frame_count,
                                    status.getFrameCount()
                                ))
                                Text(text = stringResource(
                                    R.string.disable_reason,
                                    DisableReason.fromValue(status.getDisableReason())
                                ))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest,
            ) {
                Text(stringResource(R.string.close))
            }
        }
    )
}