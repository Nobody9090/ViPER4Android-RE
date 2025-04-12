package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.AnalogXViewModel

@Composable
fun AnalogXEffect(
    viewModel: AnalogXViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val level by viewModel.level.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_analogx),
        title = stringResource(R.string.analogx),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = stringResource(R.string.analogx_level),
            summary = (level + 1).toString(),
            value = level,
            onValueChange = viewModel::setLevel,
            onValueReset = viewModel::resetLevel,
            valueRange = 0..2
        )
    }
}