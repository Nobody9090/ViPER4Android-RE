package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.vm.SpeakerOptimizationViewModel

@Composable
fun SpeakerOptimizationEffect(
    viewModel: SpeakerOptimizationViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value

    Effect(
        icon = painterResource(R.drawable.ic_speaker),
        title = "Speaker optimization",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    )
}