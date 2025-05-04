package com.aam.viper4android.ui.model

import com.aam.viper4android.driver.Session
import java.time.Instant

data class StatusSession(
    private val session: Session,
    val name: String,
) {
    val id: Int
        get() = session.id
    val startedAt: Instant
        get() = session.startedAt
    val packageName: String?
        get() = session.packageName

    var enabled: Boolean
        get() = session.effect.audioEffect.enabled
        set(value) {
            session.effect.audioEffect.setEnabled(value)
        }
}
