package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.vm.ConvolverViewModel

@Composable
fun ConvolverEffect(
    viewModel: ConvolverViewModel = hiltViewModel()
) {
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    Effect(
        icon = painterResource(R.drawable.ic_convolver),
        title = stringResource(R.string.convolver),
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    )
}