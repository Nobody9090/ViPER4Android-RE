package com.aam.viper4android.ui.effect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.Effect
import com.aam.viper4android.vm.AuditorySystemProtectionViewModel

@Composable
fun AuditorySystemProtectionEffect(
    viewModel: AuditorySystemProtectionViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value

    Effect(
        icon = painterResource(R.drawable.ic_protection),
        title = "Auditory system protection",
        checked = enabled,
        onCheckedChange = viewModel::setEnabled
    )
}