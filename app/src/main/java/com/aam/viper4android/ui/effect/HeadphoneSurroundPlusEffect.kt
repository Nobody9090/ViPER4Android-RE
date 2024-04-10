package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.HeadphoneSurroundPlusViewModel

@Composable
fun HeadphoneSurroundPlusEffect(
    viewModel: HeadphoneSurroundPlusViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val level = viewModel.level.collectAsState().value

    Effect(
        icon = painterResource(R.drawable.ic_hp_surround),
        title = stringResource(R.string.headphone_surround_plus),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        ValueSlider(
            title = stringResource(R.string.level),
            summary = (level + 1).toString(),
            value = level,
            onValueChange = viewModel::setLevel,
            onValueReset = viewModel::resetLevel,
            valueRange = 0..4
        )
    }
}