package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.Preset
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValuePicker
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.DynamicSystemViewModel

@Composable
fun DynamicSystemEffect(
    viewModel: DynamicSystemViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val deviceType = viewModel.deviceType.collectAsState().value
    val dynamicBassStrength = viewModel.dynamicBassStrength.collectAsState().value

    Effect(
        icon = painterResource(R.drawable.ic_dynamic),
        title = "Dynamic system",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValuePicker(
            title = "Device type",
            values = Preset.DynamicSystem.DeviceType.entries.map { it.name }.toTypedArray(),
            selectedIndex = deviceType.ordinal,
            onSelectedIndexChange = { viewModel.setDeviceType(Preset.DynamicSystem.DeviceType.entries[it]) },
            onSelectedIndexReset = {}
        )
        ValueSlider(
            title = "Dynamic bass strength",
            summaryUnit = "%",
            value = dynamicBassStrength,
            onValueChange = viewModel::setDynamicBassStrength,
            onValueReset = viewModel::resetDynamicBassStrength,
            valueRange = 0..100
        )
    }
}