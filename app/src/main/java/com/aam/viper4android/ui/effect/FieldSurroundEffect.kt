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
import com.aam.viper4android.vm.FieldSurroundViewModel

@Composable
fun FieldSurroundEffect(
    viewModel: FieldSurroundViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val surroundStrength by viewModel.surroundStrength.collectAsStateWithLifecycle()
    val midImageStrength by viewModel.midImageStrength.collectAsStateWithLifecycle()
    
    Effect(
        icon = painterResource(R.drawable.ic_field_surround),
        title = stringResource(R.string.field_surround),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = stringResource(R.string.field_surround_surround_strength),
            summary = (surroundStrength + 1).toString(),
            value = surroundStrength,
            onValueChange = viewModel::setSurroundStrength,
            onValueReset = viewModel::resetSurroundStrength,
            valueRange = 0..8
        )
        ValueSlider(
            title = stringResource(R.string.field_surround_mid_image_strength),
            summary = (midImageStrength + 1).toString(),
            value = midImageStrength,
            onValueChange = viewModel::setMidImageStrength,
            onValueReset = viewModel::resetMidImageStrength,
            valueRange = 0..10
        )
    }
}