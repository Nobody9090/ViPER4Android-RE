package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.ui.component.ValueSlider
import com.aam.viper4android.vm.FieldSurroundViewModel

@Composable
fun FieldSurroundEffect(
    viewModel: FieldSurroundViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val surroundStrength = viewModel.surroundStrength.collectAsState().value
    val midImageStrength = viewModel.midImageStrength.collectAsState().value
    
    Effect(
        icon = painterResource(R.drawable.ic_field_surround),
        title = "Field surround",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    ) {
        Column {
            ValueSlider(
                title = "Surround strength",
                summary = (surroundStrength + 1).toString(),
                value = surroundStrength,
                onValueChange = viewModel::setSurroundStrength,
                valueRange = 0..8
            )
            ValueSlider(
                title = "Mid image strength",
                summary = (midImageStrength + 1).toString(),
                value = midImageStrength,
                onValueChange = viewModel::setMidImageStrength,
                valueRange = 0..10
            )
        }
    }
}