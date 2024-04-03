package com.aam.viper4android

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun PresetDialog(onDismissRequest: () -> Unit) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = RoundedCornerShape(28.dp)
                )
        ) {
            Text(
                text = "Presets",
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            var query by rememberSaveable { mutableStateOf("") }
            val isError = query.isNotEmpty() && query.isBlank()

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier.padding(horizontal = 24.dp),
                label = {
                    Text("Search")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                supportingText = if (isError) {
                    {
                        Text("No presets found")
                    }
                } else null,
                isError = isError
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .selectableGroup()
            ) {
                val values = arrayOf("Default", "Bass Boost", "Treble Boost", "Vocal Boost", "Custom", "Custom 2", "Custom 3", "Custom 4", "Custom 5", "Custom 6", "Custom 7", "Custom 8", "Custom 9", "Custom 10")
                var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
                values.forEachIndexed { index, value ->
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .fillMaxWidth()
                            .selectable(
                                selected = index == selectedIndex,
                                role = Role.RadioButton,
                                onClick = { selectedIndex = index }
                            )
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = index == selectedIndex,
                            onClick = null
                        )
                        Text(text = value)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                OutlinedButton(
                    onClick = { /*TODO*/ }
                ) {
                    Text("Import")
                }

                Button(
                    onClick = onDismissRequest
                ) {
                    Text("Close")
                }
            }
        }
    }
}