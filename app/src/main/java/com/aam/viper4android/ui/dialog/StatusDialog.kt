package com.aam.viper4android.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.ElapsedTimeSince
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

            val sessions by statusViewModel.sessions.collectAsStateWithLifecycle()
            if (sessions.isEmpty()) {
                Text(text = stringResource(id = R.string.no_active_sessions))
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(sessions) { session ->
                        key(session.id) {
                            var enabled by remember(session) {
                                mutableStateOf(session.enabled)
                            }
                            var openCloseSessionDialog by remember { mutableStateOf(false) }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = session.name,
                                            modifier = Modifier.weight(1f, fill = false),
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1,
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                        ElapsedTimeSince(
                                            startInstant = session.startedAt,
                                        ) { elapsedTime ->
                                            Text(
                                                text = " · $elapsedTime",
                                                maxLines = 1,
                                                style = MaterialTheme.typography.labelLarge,
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Session ID: ${session.id}",
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        openCloseSessionDialog = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Close session",
                                    )
                                }
                                Switch(
                                    checked = enabled,
                                    onCheckedChange = {
                                        enabled = it
                                        session.enabled = it
                                    },
                                )
                            }

                            if (openCloseSessionDialog) {
                                AlertDialog(
                                    onDismissRequest = { openCloseSessionDialog = false },
                                    icon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = "Close session",
                                        )
                                    },
                                    title = { Text(text = "Close session ${session.id}") },
                                    text = {
                                        Text(
                                            text = "Do you want to close the session from ${session.name}? This action cannot be undone.",
                                        )
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                statusViewModel.closeSession(session)
                                                openCloseSessionDialog = false
                                            }
                                        ) {
                                            Text("Confirm")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = { openCloseSessionDialog = false }
                                        ) {
                                            Text("Cancel")
                                        }
                                    }
                                )
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