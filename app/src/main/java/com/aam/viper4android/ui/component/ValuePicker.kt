package com.aam.viper4android.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ValuePicker(
    title: String,
    values: Array<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .padding(horizontal = 24.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            letterSpacing = 0.5.sp
        )
        Text(
            text = values[selectedIndex],
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            letterSpacing = 0.25.sp
        )
    }

    if (showDialog) {
        ValuePickerDialog(
            title,
            values,
            selectedIndex,
            onSelectedIndexChange,
            onDismissRequest = { showDialog = false })
    }
}

@Composable
private fun ValuePickerDialog(
    title: String,
    values: Array<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = title)
        },
        text = {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                values.forEachIndexed { index, value ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectedIndexChange(index) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = index == selectedIndex,
                            onClick = { onSelectedIndexChange(index) })
                        Text(text = value)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismissRequest
            ) {
                Text("Close")
            }
        }
    )
}