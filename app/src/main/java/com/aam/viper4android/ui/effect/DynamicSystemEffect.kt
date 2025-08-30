package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.R
import com.aam.viper4android.driver.Preset
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValuePicker
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.DynamicSystemViewModel

@Composable
fun DynamicSystemEffect(
    viewModel: DynamicSystemViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val deviceType by viewModel.deviceType.collectAsStateWithLifecycle()
    val dynamicBassStrength by viewModel.dynamicBassStrength.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_dynamic),
        title = stringResource(R.string.dynamic_system),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValuePicker(
            title = stringResource(R.string.dynamic_system_device_type),
            values = Preset.DynamicSystem.DeviceType.entries.map { it.name }.toTypedArray(),
            selectedIndex = deviceType.ordinal,
            onSelectedIndexChange = { viewModel.setDeviceType(Preset.DynamicSystem.DeviceType.entries[it]) },
            onSelectedIndexReset = {}
        )
        ValueSlider(
            title = stringResource(R.string.dynamic_system_dynamic_bass_strength),
            summaryUnit = "%",
            value = dynamicBassStrength,
            onValueChange = viewModel::setDynamicBassStrength,
            onValueReset = viewModel::resetDynamicBassStrength,
            valueRange = 0..100
        )
    }
}