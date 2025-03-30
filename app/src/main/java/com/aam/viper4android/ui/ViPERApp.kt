package com.aam.viper4android.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aam.viper4android.ui.screen.MainScreen
import com.aam.viper4android.ui.onboarding.OnboardingScreen
import com.aam.viper4android.ui.screen.SettingsScreen
import com.aam.viper4android.vm.MainViewModel

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Main : Screen("main")
    object Settings : Screen("settings")
}

@Composable
fun ViPERApp(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val startDestination = viewModel.startDestination.collectAsState().value
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onOnboardingComplete = viewModel::setOnboardingShown
            )
        }
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateUp = navController::popBackStack
            )
        }
    }
}