package com.aam.viper4android

data class Preset(
    var name: String = DEFAULT_NAME,
    var enabled: Boolean = DEFAULT_ENABLED,
    val analogX: AnalogX = AnalogX(),
    val auditorySystemProtection: AuditorySystemProtection = AuditorySystemProtection(),
    val convolver: Convolver = Convolver(),
    val differentialSurround: DifferentialSurround = DifferentialSurround(),
    val dynamicSystem: DynamicSystem = DynamicSystem(),
    val fetCompressor: FETCompressor = FETCompressor(),
    val fieldSurround: FieldSurroundEffect = FieldSurroundEffect(),
    val firEqualizer: FIREqualizer = FIREqualizer(),
    val headphoneSurroundPlus: HeadphoneSurroundPlus = HeadphoneSurroundPlus(),
    val masterLimiter: MasterLimiter = MasterLimiter(),
    val playbackGainControl: PlaybackGainControl = PlaybackGainControl(),
    val reverberation: Reverberation = Reverberation(),
    val speakerOptimization: SpeakerOptimization = SpeakerOptimization(),
    val spectrumExtension: SpectrumExtension = SpectrumExtension(),
    val tubeSimulator6N1J: TubeSimulator6N1J = TubeSimulator6N1J(),
    val viperBass: ViPERBass = ViPERBass(),
    val viperClarity: ViPERClarity = ViPERClarity(),
    val viperDdc: ViPERDDC = ViPERDDC(),
) {
    data class AnalogX(
        var enabled: Boolean = DEFAULT_ENABLED,
        var level: Int = DEFAULT_LEVEL
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_LEVEL: Int = 1
        }
    }

    data class AuditorySystemProtection(
        var enabled: Boolean = DEFAULT_ENABLED,
        var level: Int = DEFAULT_LEVEL
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_LEVEL: Int = 1
        }
    }

    data class Convolver(
        var enabled: Boolean = DEFAULT_ENABLED,
        var irPath: String = DEFAULT_IR_PATH,
        var crossChannel: Int = DEFAULT_CROSS_CHANNEL
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_IR_PATH: String = ""
            const val DEFAULT_CROSS_CHANNEL: Int = 0
        }
    }

    data class DifferentialSurround(
        var enabled: Boolean = DEFAULT_ENABLED,
        var delay: Int = DEFAULT_DELAY
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_DELAY: Int = 5
        }
    }

    data class DynamicSystem(
        var enabled: Boolean = DEFAULT_ENABLED,
        var deviceType: Int = DEFAULT_DEVICE_TYPE,
        var dynamicBassStrength: Int = DEFAULT_DYNAMIC_BASS_STRENGTH
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_DEVICE_TYPE: Int = 0
            const val DEFAULT_DYNAMIC_BASS_STRENGTH: Int = 0
        }
    }

    data class FETCompressor(
        var enabled: Boolean = DEFAULT_ENABLED,
        var operatingThreshold: Int = DEFAULT_OPERATING_THRESHOLD,
        var compressionRatio: Int = DEFAULT_COMPRESSION_RATIO,
        var autoKnee: Boolean = DEFAULT_AUTO_KNEE,
        var inflection: Int = DEFAULT_INFLECTION,
        var inflectionPointGain: Int = DEFAULT_INFLECTION_POINT_GAIN,
        var autoGain: Boolean = DEFAULT_AUTO_GAIN,
        var gain: Int = DEFAULT_GAIN,
        var autoAttack: Boolean = DEFAULT_AUTO_ATTACK,
        var attack: Int = DEFAULT_ATTACK,
        var maximumAttack: Int = DEFAULT_MAXIMUM_ATTACK,
        var autoRelease: Boolean = DEFAULT_AUTO_RELEASE,
        var release: Int = DEFAULT_RELEASE,
        var maximumRelease: Int = DEFAULT_MAXIMUM_RELEASE,
        var crest: Int = DEFAULT_CREST,
        var adapt: Int = DEFAULT_ADAPT,
        var clippingPrevention: Boolean = DEFAULT_CLIPPING_PREVENTION
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_OPERATING_THRESHOLD: Int = 100
            const val DEFAULT_COMPRESSION_RATIO: Int = 100
            const val DEFAULT_AUTO_KNEE: Boolean = true
            const val DEFAULT_INFLECTION: Int = 0
            const val DEFAULT_INFLECTION_POINT_GAIN: Int = 0
            const val DEFAULT_AUTO_GAIN: Boolean = true
            const val DEFAULT_GAIN: Int = 0
            const val DEFAULT_AUTO_ATTACK: Boolean = true
            const val DEFAULT_ATTACK: Int = 20
            const val DEFAULT_MAXIMUM_ATTACK: Int = 80
            const val DEFAULT_AUTO_RELEASE: Boolean = true
            const val DEFAULT_RELEASE: Int = 50
            const val DEFAULT_MAXIMUM_RELEASE: Int = 100
            const val DEFAULT_CREST: Int = 100
            const val DEFAULT_ADAPT: Int = 50
            const val DEFAULT_CLIPPING_PREVENTION: Boolean = true
        }
    }

    data class FieldSurroundEffect(
        var enabled: Boolean = DEFAULT_ENABLED,
        var surroundStrength: Int = DEFAULT_SURROUND_STRENGTH,
        var midImageStrength: Int = DEFAULT_MID_IMAGE_STRENGTH,
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_SURROUND_STRENGTH: Int = 0
            const val DEFAULT_MID_IMAGE_STRENGTH: Int = 5
        }
    }

    data class FIREqualizer(
        var enabled: Boolean = DEFAULT_ENABLED,
        var gains: List<Int> = DEFAULT_GAINS
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            val DEFAULT_GAINS: List<Int> = List(10) { 0 }
        }
    }

    data class HeadphoneSurroundPlus(
        var enabled: Boolean = DEFAULT_ENABLED,
        var level: Int = DEFAULT_LEVEL
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_LEVEL: Int = 1
        }
    }

    data class MasterLimiter(
        var outputGain: Int = DEFAULT_OUTPUT_GAIN,
        var outputPan: Int = DEFAULT_OUTPUT_PAN,
        var thresholdLimit: Int = DEFAULT_THRESHOLD_LIMIT
    ) {
        companion object {
            const val DEFAULT_OUTPUT_GAIN: Int = 100
            const val DEFAULT_OUTPUT_PAN: Int = 50
            const val DEFAULT_THRESHOLD_LIMIT: Int = 100
        }
    }

    data class PlaybackGainControl(
        var enabled: Boolean = DEFAULT_ENABLED,
        var strength: Int = DEFAULT_STRENGTH,
        var maximumGain: Int = DEFAULT_MAXIMUM_GAIN,
        var outputThreshold: Int = DEFAULT_OUTPUT_THRESHOLD
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_STRENGTH: Int = 0
            const val DEFAULT_MAXIMUM_GAIN: Int = 3
            const val DEFAULT_OUTPUT_THRESHOLD: Int = 3
        }
    }

    data class Reverberation(
        var enabled: Boolean = DEFAULT_ENABLED,
        var roomSize: Int = DEFAULT_ROOM_SIZE,
        var soundField: Int = DEFAULT_SOUND_FIELD,
        var damping: Int = DEFAULT_DAMPING,
        var wetSignal: Int = DEFAULT_WET_SIGNAL,
        var drySignal: Int = DEFAULT_DRY_SIGNAL
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_ROOM_SIZE: Int = 0
            const val DEFAULT_SOUND_FIELD: Int = 0
            const val DEFAULT_DAMPING: Int = 0
            const val DEFAULT_WET_SIGNAL: Int = 0
            const val DEFAULT_DRY_SIGNAL: Int = 50
        }
    }

    data class SpeakerOptimization(
        var enabled: Boolean = DEFAULT_ENABLED
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
        }
    }

    data class SpectrumExtension(
        var enabled: Boolean = DEFAULT_ENABLED,
        var strength: Int = DEFAULT_STRENGTH
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_STRENGTH: Int = 10
        }
    }

    data class TubeSimulator6N1J(
        var enabled: Boolean = DEFAULT_ENABLED
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
        }
    }

    data class ViPERBass(
        var enabled: Boolean = DEFAULT_ENABLED,
        var mode: Int = DEFAULT_MODE,
        var frequency: Int = DEFAULT_FREQUENCY,
        var gain: Int = DEFAULT_GAIN,
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_MODE: Int = 0
            const val DEFAULT_FREQUENCY: Int = 70
            const val DEFAULT_GAIN: Int = 1
        }
    }

    data class ViPERClarity(
        var enabled: Boolean = DEFAULT_ENABLED,
        var mode: Int = DEFAULT_MODE,
        var gain: Int = DEFAULT_GAIN,
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_MODE: Int = 0
            const val DEFAULT_GAIN: Int = 1
        }
    }

    data class ViPERDDC(
        var enabled: Boolean = DEFAULT_ENABLED,
        var ddcPath: String = DEFAULT_DDC_PATH
    ) {
        companion object {
            const val DEFAULT_ENABLED: Boolean = false
            const val DEFAULT_DDC_PATH: String = ""
        }
    }

    companion object {
        const val DEFAULT_NAME: String = "Untitled preset"
        const val DEFAULT_ENABLED: Boolean = false
    }
}