package com.aam.viper4android.ui.effect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.EqualizerPreview
import com.aam.viper4android.ui.component.ScrollableEqualizerEditor
import com.aam.viper4android.vm.FIREqualizerViewModel

@Composable
fun FIREqualizerEffect(
    viewModel: FIREqualizerViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    
    Effect(
        icon = painterResource(R.drawable.ic_equalizer),
        title = "FIR equalizer",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        Column {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 22.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Custom") }
                )
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Bass reduce") }
                )
                FilterChip(
                    selected = true,
                    onClick = { /*TODO*/ },
                    label = { Text("Flat") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                )
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Classic") }
                )
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Jazz") }
                )
                FilterChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text("Pop") }
                )
            }
            Spacer(Modifier.height(18.dp))
            ScrollableEqualizerEditor(
                modifier = Modifier.height(500.dp).fillMaxWidth(),
                gains = floatArrayOf(
                    0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f
                )
            )
        }
    }
}