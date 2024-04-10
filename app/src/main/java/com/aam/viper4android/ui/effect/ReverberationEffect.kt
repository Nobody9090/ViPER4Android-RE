package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
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
    val enabled = viewModel.enabled.collectAsState().value
    val roomSize = viewModel.roomSize.collectAsState().value
    val soundField = viewModel.soundField.collectAsState().value
    val damping = viewModel.damping.collectAsState().value
    val wetSignal = viewModel.wetSignal.collectAsState().value
    val drySignal = viewModel.drySignal.collectAsState().value

    Effect(
        icon = painterResource(R.drawable.ic_reverb),
        title = "Reverberation",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = "Room size",
            summary = roomSizeSummaryValues[roomSize / 10],
            summaryUnit = "m²",
            value = roomSize,
            onValueChange = viewModel::setRoomSize,
            onValueReset = viewModel::resetRoomSize,
            valueRange = 0..100,
            steps = roomSizeSummaryValues.size - 2
        )
        ValueSlider(
            title = "Sound field",
            summary = soundFieldSummaryValues[soundField / 10],
            summaryUnit = "m",
            value = soundField,
            onValueChange = viewModel::setSoundField,
            onValueReset = viewModel::resetSoundField,
            valueRange = 0..100,
            steps = soundFieldSummaryValues.size - 2
        )
        ValueSlider(
            title = "Damping factor",
            summaryUnit = "%",
            value = damping,
            onValueChange = viewModel::setDamping,
            onValueReset = viewModel::resetDamping,
            valueRange = 0..100
        )
        ValueSlider(
            title = "Wet signal",
            summaryUnit = "%",
            value = wetSignal,
            onValueChange = viewModel::setWetSignal,
            onValueReset = viewModel::resetWetSignal,
            valueRange = 0..100
        )
        ValueSlider(
            title = "Dry signal",
            summaryUnit = "%",
            value = drySignal,
            onValueChange = viewModel::setDrySignal,
            onValueReset = viewModel::resetDrySignal,
            valueRange = 0..100
        )
    }
}