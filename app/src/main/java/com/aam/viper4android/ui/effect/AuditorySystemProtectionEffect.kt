package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.AuditorySystemProtectionViewModel

@Composable
fun AuditorySystemProtectionEffect(
    viewModel: AuditorySystemProtectionViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_protection),
        title = stringResource(R.string.auditory_system_protection),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        var value by remember { mutableIntStateOf(1) }
        ValueSlider(
            title = "Binaural level",
            value = value,
            onValueChange = { value = it },
            onValueReset = { value = 1 },
            valueRange = 1..3,
        )
    }
}