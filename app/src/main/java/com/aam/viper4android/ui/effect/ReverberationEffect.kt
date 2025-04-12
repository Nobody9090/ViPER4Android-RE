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
import com.aam.viper4android.vm.ReverberationViewModel

private val roomSizeSummaryValues = arrayOf(
    "25",
    "36",
    "49",
    "64",
    "81",
    "100",
    "121",
    "203",
    "347",
    "652",
    "1200"
)

private val soundFieldSummaryValues = arrayOf(
    "5",
    "6",
    "7",
    "8",
    "9",
    "10",
    "11",
    "14",
    "19",
    "26",
    "36"
)

@Composable
fun ReverberationEffect(
    viewModel: ReverberationViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val roomSize by viewModel.roomSize.collectAsStateWithLifecycle()
    val soundField by viewModel.soundField.collectAsStateWithLifecycle()
    val damping by viewModel.damping.collectAsStateWithLifecycle()
    val wetSignal by viewModel.wetSignal.collectAsStateWithLifecycle()
    val drySignal by viewModel.drySignal.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_reverb),
        title = stringResource(R.string.reverberation),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = stringResource(R.string.reverberation_room_size),
            summary = roomSizeSummaryValues[roomSize / 10],
            summaryUnit = "m²",
            value = roomSize,
            onValueChange = viewModel::setRoomSize,
            onValueReset = viewModel::resetRoomSize,
            valueRange = 0..100,
            steps = roomSizeSummaryValues.size - 2
        )
        ValueSlider(
            title = stringResource(R.string.reverberation_sound_field),
            summary = soundFieldSummaryValues[soundField / 10],
            summaryUnit = "m",
            value = soundField,
            onValueChange = viewModel::setSoundField,
            onValueReset = viewModel::resetSoundField,
            valueRange = 0..100,
            steps = soundFieldSummaryValues.size - 2
        )
        ValueSlider(
            title = stringResource(R.string.reverberation_damping_factor),
            summaryUnit = "%",
            value = damping,
            onValueChange = viewModel::setDamping,
            onValueReset = viewModel::resetDamping,
            valueRange = 0..100
        )
        ValueSlider(
            title = stringResource(R.string.reverberation_wet_signal),
            summaryUnit = "%",
            value = wetSignal,
            onValueChange = viewModel::setWetSignal,
            onValueReset = viewModel::resetWetSignal,
            valueRange = 0..100
        )
        ValueSlider(
            title = stringResource(R.string.reverberation_dry_signal),
            summaryUnit = "%",
            value = drySignal,
            onValueChange = viewModel::setDrySignal,
            onValueReset = viewModel::resetDrySignal,
            valueRange = 0..100
        )
    }
}