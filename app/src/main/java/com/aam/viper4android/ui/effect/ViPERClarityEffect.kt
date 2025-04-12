package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValuePicker
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.ViPERClarityViewModel

private val gainSummaryValues = arrayOf(
    "0.0",
    "3.5",
    "6.0",
    "8.0",
    "10.0",
    "11.0",
    "12.0",
    "13.0",
    "14.0",
    "14.8",
)

@Composable
fun ViPERClarityEffect(
    viewModel: ViPERClarityViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val mode by viewModel.mode.collectAsStateWithLifecycle()
    val gain by viewModel.gain.collectAsStateWithLifecycle()
    
    Effect(
        icon = painterResource(R.drawable.ic_clarity),
        title = stringResource(R.string.viper_clarity),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValuePicker(
            title = stringResource(R.string.clarity_mode),
            values = arrayOf(
                stringResource(R.string.natural),
                stringResource(R.string.ozone_plus),
                stringResource(R.string.xhifi),
            ),
            selectedIndex = mode,
            onSelectedIndexChange = viewModel::setMode,
            onSelectedIndexReset = viewModel::resetMode
        )
        ValueSlider(
            title = stringResource(R.string.clarity_gain),
            summary = gainSummaryValues[gain],
            summaryUnit = "dB",
            value = gain,
            onValueChange = viewModel::setGain,
            onValueReset = viewModel::resetGain,
            valueRange = gainSummaryValues.indices
        )
    }
}