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
import com.aam.viper4android.vm.DifferentialSurroundViewModel

@Composable
fun DifferentialSurroundEffect(
    viewModel: DifferentialSurroundViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val delay by viewModel.delay.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_diff_surround),
        title = stringResource(R.string.differential_surround),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = stringResource(R.string.differential_surround_delay),
            summary = (delay + 1).toString(),
            summaryUnit = "ms",
            value = delay,
            onValueChange = viewModel::setDelay,
            onValueReset = viewModel::resetDelay,
            valueRange = 0..19
        )
    }
}