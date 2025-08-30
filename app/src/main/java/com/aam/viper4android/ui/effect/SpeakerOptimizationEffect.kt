package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.vm.SpeakerOptimizationViewModel

@Composable
fun SpeakerOptimizationEffect(
    viewModel: SpeakerOptimizationViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_speaker),
        title = stringResource(R.string.speaker_optimization),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    )
}