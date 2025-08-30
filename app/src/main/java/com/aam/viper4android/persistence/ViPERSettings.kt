package com.aam.viper4android.persistence

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViPERSettings @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _legacyMode = MutableStateFlow(false)
    val legacyMode = _legacyMode.asStateFlow()

    init {
        scope.launch {
            legacyMode.collect {

            }
        }
    }

    fun setLegacyMode(legacyMode: Boolean) {
        _legacyMode.value = legacyMode
    }
}