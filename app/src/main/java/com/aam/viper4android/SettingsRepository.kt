package com.aam.viper4android

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext context: Context,
) {
    var serviceAlwaysActive: Boolean
        get() = false // todo
        set(value) {
            // todo
        }
}