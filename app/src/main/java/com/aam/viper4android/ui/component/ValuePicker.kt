package com.aam.viper4android.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

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
                text = title,
                modifier = Modifier.padding(24.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .selectableGroup()
            ) {
                values.forEachIndexed { index, value ->
                    Row(
                        modifier = Modifier
                            .height(56.dp)
                            .fillMaxWidth()
                            .selectable(
                                selected = index == selectedIndex,
                                role = Role.RadioButton,
                                onClick = { onSelectedIndexChange(index) }
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
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDismissRequest
                ) {
                    Text("Close")
                }
            }
        }
    }
}