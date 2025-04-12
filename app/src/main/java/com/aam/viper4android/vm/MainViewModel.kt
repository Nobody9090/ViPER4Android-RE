package com.aam.viper4android.vm

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import com.aam.viper4android.driver.ViPEREffect
import com.aam.viper4android.driver.ViPERManager
import com.aam.viper4android.ui.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "main")
private val ONBOARDING_SHOWN = booleanPreferencesKey("onboarding_shown")

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val dataStore = context.dataStore

    private val _startDestination = MutableStateFlow(findStartDestination())
    val startDestination = _startDestination.asStateFlow()

    val enabled = viperManager.enabled

    private fun findStartDestination(): String {
        return if (shouldShowOnboarding()) {
            Screen.Onboarding.route
        } else {
            Screen.Main.route
        }
    }

    private fun shouldShowOnboarding(): Boolean {
        return !ViPEREffect.isAvailable || runBlocking { dataStore.data.first()[ONBOARDING_SHOWN] != true }
    }

    fun setOnboardingShown() {
        runBlocking { dataStore.edit { it[ONBOARDING_SHOWN] = true } }
        _startDestination.value = findStartDestination()
    }

    fun setEnabled(enabled: Boolean) = viperManager.setEnabled(enabled)
}