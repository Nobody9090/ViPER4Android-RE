package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.SpectrumExtensionViewModel

@Composable
fun SpectrumExtensionEffect(
    viewModel: SpectrumExtensionViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val strength = viewModel.strength.collectAsState().value

    Effect(
        icon = painterResource(R.drawable.ic_spectrum),
        title = "Spectrum extension",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = "Strength",
            summaryUnit = "%",
            value = strength,
            onValueChange = viewModel::setStrength,
            onValueReset = viewModel::resetStrength,
            valueRange = 0..100
        )
    }
}