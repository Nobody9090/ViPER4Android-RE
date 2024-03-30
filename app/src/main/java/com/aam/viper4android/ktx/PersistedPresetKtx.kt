package com.aam.viper4android.ktx

import com.aam.viper4android.Preset
import com.aam.viper4android.persistence.model.PersistedPreset

fun PersistedPreset.toPreset(): Preset {
    return Preset()
}