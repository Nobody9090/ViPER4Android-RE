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
import com.aam.viper4android.vm.MasterLimiterViewModel

private val outputGainSummaryValues = arrayOf(
    "-40.0",
    "-26.0",
    "-20.0",
    "-14.0",
    "-10.5",
    "-8.0",
    "-6.0",
    "-4.4",
    "-3.0",
    "-1.9",
    "-1.0",
    "0.0",
    "0.8",
    "1.6",
    "2.3",
    "2.9",
    "3.5",
    "4.1",
    "4.6",
    "5.1",
    "5.6",
    "6.0"
)

private val thresholdLimitSummaryValues = arrayOf(
    "-10.5",
    "-6.0",
    "-3.0",
    "-1.9",
    "-1.0",
    "0.0"
)

@Composable
fun MasterLimiterEffect(
    viewModel: MasterLimiterViewModel = hiltViewModel()
) {
    val outputGain by viewModel.outputGain.collectAsStateWithLifecycle()
    val outputPan by viewModel.outputPan.collectAsStateWithLifecycle()
    val thresholdLimit by viewModel.thresholdLimit.collectAsStateWithLifecycle()
    
    Effect(
        icon = painterResource(R.drawable.ic_master_limiter),
        title = stringResource(R.string.master_limiter),
    ) {
        ValueSlider(
            title = stringResource(R.string.master_limiter_output_gain),
            summary = outputGainSummaryValues[outputGain],
            summaryUnit = "dB",
            value = outputGain,
            onValueChange = viewModel::setOutputGain,
            onValueReset = viewModel::resetOutputGain,
            valueRange = outputGainSummaryValues.indices
        )
        ValueSlider(
            title = stringResource(R.string.master_limiter_output_pan),
            summary = "${100 - outputPan}:${outputPan}",
            value = outputPan,
            onValueChange = viewModel::setOutputPan,
            onValueReset = viewModel::resetOutputPan,
            valueRange = 0..100
        )
        ValueSlider(
            title = stringResource(R.string.master_limiter_threshold_limit),
            value = thresholdLimit,
            summary = thresholdLimitSummaryValues[thresholdLimit],
            summaryUnit = "dB",
            onValueChange = viewModel::setThresholdLimit,
            onValueReset = viewModel::resetThresholdLimit,
            valueRange = thresholdLimitSummaryValues.indices
        )
    }
}