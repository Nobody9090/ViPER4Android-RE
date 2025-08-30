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
import com.aam.viper4android.vm.SpectrumExtensionViewModel

@Composable
fun SpectrumExtensionEffect(
    viewModel: SpectrumExtensionViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val strength by viewModel.strength.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_spectrum),
        title = stringResource(R.string.spectrum_extension),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = stringResource(R.string.spectrum_extension_strength),
            summaryUnit = "%",
            value = strength,
            onValueChange = viewModel::setStrength,
            onValueReset = viewModel::resetStrength,
            valueRange = 0..100
        )
    }
}