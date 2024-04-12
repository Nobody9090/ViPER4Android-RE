package com.aam.viper4android.persistence.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aam.viper4android.driver.Preset

@Entity(tableName = "presets")
data class PersistedPreset(
    @PrimaryKey @ColumnInfo(name = "device_id") val deviceId: String,
    @ColumnInfo(name = "enabled") val enabled: Boolean,
    @Embedded(prefix = "analog_x") val analogX: AnalogX,
    @Embedded(prefix = "auditory_system_protection") val auditorySystemProtection: AuditorySystemProtection,
    @Embedded(prefix = "convolver") val convolver: Convolver,
    @Embedded(prefix = "differential_surround") val differentialSurround: DifferentialSurround,
    @Embedded(prefix = "dynamic_system") val dynamicSystem: DynamicSystem,
    @Embedded(prefix = "fet_compressor") val fetCompressor: FETCompressor,
    @Embedded(prefix = "field_surround_effect") val fieldSurroundEffect: FieldSurroundEffect,
    @Embedded(prefix = "fir_equalizer") val firequalizer: FIREqualizer,
    @Embedded(prefix = "headphone_surround_plus") val headphoneSurroundPlus: HeadphoneSurroundPlus,
    @Embedded(prefix = "master_limiter") val masterLimiter: MasterLimiter,
    @Embedded(prefix = "playback_gain_control") val playbackGainControl: PlaybackGainControl,
    @Embedded(prefix = "reverberation") val reverberation: Reverberation,
    @Embedded(prefix = "speaker_optimization") val speakerOptimization: SpeakerOptimization,
    @Embedded(prefix = "spectrum_extension") val spectrumExtension: SpectrumExtension,
    @Embedded(prefix = "tube_simulator_6n1j") val tubeSimulator6N1J: TubeSimulator6N1J,
    @Embedded(prefix = "viper_bass") val viperBass: ViPERBass,
    @Embedded(prefix = "viper_clarity") val viperClarity: ViPERClarity,
    @Embedded(prefix = "viper_ddc") val viperDDC: ViPERDDC,
) {
    data class AnalogX(
        val field: String,
    )

    data class AuditorySystemProtection(
        val field: String,
    )

    data class Convolver(
        val field: String,
    )

    data class DifferentialSurround(
        val field: String,
    )

    data class DynamicSystem(
        val field: String,
    )

    data class FETCompressor(
        val field: String,
    )

    data class FieldSurroundEffect(
        val field: String,
    )

    data class FIREqualizer(
        val field: String,
    )

    data class HeadphoneSurroundPlus(
        val field: String,
    )

    data class MasterLimiter(
        val field: String,
    )

    data class PlaybackGainControl(
        val field: String,
    )

    data class Reverberation(
        val field: String,
    )

    data class SpeakerOptimization(
        val field: String,
    )

    data class SpectrumExtension(
        val field: String,
    )

    data class TubeSimulator6N1J(
        val field: String,
    )

    data class ViPERBass(
        val field: String,
    )

    data class ViPERClarity(
        val field: String,
    )

    data class ViPERDDC(
        val field: String,
    )

    companion object {
        fun fromPreset(preset: Preset): PersistedPreset {
            return PersistedPreset(
                deviceId = "",
                enabled = preset.enabled,
                analogX = AnalogX(""), // TODO
                auditorySystemProtection = AuditorySystemProtection(""), // TODO
                convolver = Convolver(""), // TODO
                differentialSurround = DifferentialSurround(""), // TODO
                dynamicSystem = DynamicSystem(""), // TODO
                fetCompressor = FETCompressor(""), // TODO
                fieldSurroundEffect = FieldSurroundEffect(""), // TODO
                firequalizer = FIREqualizer(""), // TODO
                headphoneSurroundPlus = HeadphoneSurroundPlus(""), // TODO
                masterLimiter = MasterLimiter(""), // TODO
                playbackGainControl = PlaybackGainControl(""), // TODO
                reverberation = Reverberation(""), // TODO
                speakerOptimization = SpeakerOptimization(""), // TODO
                spectrumExtension = SpectrumExtension(""), // TODO
                tubeSimulator6N1J = TubeSimulator6N1J(""), // TODO
                viperBass = ViPERBass(""), // TODO
                viperClarity = ViPERClarity(""), // TODO
                viperDDC = ViPERDDC("") // TODO
            )
        }
    }
}
