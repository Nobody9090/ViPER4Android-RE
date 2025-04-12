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
import com.aam.viper4android.vm.PlaybackGainControlViewModel

@Composable
fun PlaybackGainControlEffect(
    viewModel: PlaybackGainControlViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
//    val strength by viewModel.strength.collectAsStateWithLifecycle()
//    val gain by viewModel.gain.collectAsStateWithLifecycle()
//    val threshold by viewModel.threshold.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_playback_gain),
        title = stringResource(R.string.playback_gain_control),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = stringResource(R.string.strength),
            value = 0,
            onValueChange = { /*viewModel::setStrength*/ },
            onValueReset = {},
            valueRange = 0..2
        )
        ValueSlider(
            title = stringResource(R.string.maximum_gain),
            value = 3,
            onValueChange = { /*viewModel::setGain*/ },
            onValueReset = {},
            valueRange = 0..10
        )
        ValueSlider(
            title = stringResource(R.string.output_threshold),
            value = 3,
            onValueChange = { /*viewModel::setThreshold*/ },
            onValueReset = {},
            valueRange = 0..5
        )
    }
}