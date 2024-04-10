package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.DifferentialSurroundViewModel

@Composable
fun DifferentialSurroundEffect(
    viewModel: DifferentialSurroundViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val delay = viewModel.delay.collectAsState().value

    Effect(
        icon = painterResource(R.drawable.ic_diff_surround),
        title = "Differential surround",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = "Delay",
            summary = (delay + 1).toString(),
            summaryUnit = "ms",
            value = delay,
            onValueChange = viewModel::setDelay,
            onValueReset = viewModel::resetDelay,
            valueRange = 0..19
        )
    }
}