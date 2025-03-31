package com.aam.viper4android.driver

import com.aam.viper4android.ktx.AudioEffectKtx
import com.aam.viper4android.ktx.getBooleanParameter
import com.aam.viper4android.ktx.getByteArrayParameter
import com.aam.viper4android.ktx.getIntParameter
import com.aam.viper4android.ktx.getUByteParameter
import com.aam.viper4android.ktx.getUIntParameter
import com.aam.viper4android.ktx.getULongParameter
import com.aam.viper4android.ktx.putFloatArray
import com.aam.viper4android.ktx.putUByte
import com.aam.viper4android.ktx.putUInt
import com.aam.viper4android.ktx.putUShort
import com.aam.viper4android.ktx.setBooleanParameter
import com.aam.viper4android.ktx.setByteArrayParameter
import com.aam.viper4android.ktx.setUByteArrayParameter
import com.aam.viper4android.ktx.setUByteParameter
import com.aam.viper4android.ktx.setUShortParameter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

class ViPEREffect(sessionId: Int) {
    val audioEffect = AudioEffectKtx(VIPER_TYPE_UUID, VIPER_UUID, 0, sessionId)

    val status = Status()
    val masterLimiter = MasterLimiter()
//    val playbackGainControl = PlaybackGainControl()
//    val fetCompressor = FETCompressor()
    val viperDDC = ViPERDDC()
    val spectrumExtension = SpectrumExtension()
    val iirEqualizer = IIREqualizer()
//    val convolver = Convolver()
    val fieldSurround = FieldSurround()
    val differentialSurround = DifferentialSurround()
//    val headphoneSurroundPlus = HeadphoneSurroundPlus()
    val reverberation = Reverberation()
    val dynamicSystem = DynamicSystem()
    val tubeSimulator = TubeSimulator()
    val viperBass = ViPERBass()
    val viperClarity = ViPERClarity()
    val auditorySystemProtection = AuditorySystemProtection()
    val analogX = AnalogX()
    val speakerOptimization = SpeakerOptimization()

    fun reset() = audioEffect.setBooleanParameter(PARAM_SET_RESET, true)

    inner class Status {
        fun getEnabled() = audioEffect.getBooleanParameter(PARAM_GET_ENABLED)
        fun getFrameCount() = audioEffect.getULongParameter(PARAM_GET_FRAME_COUNT)
        fun getVersion() = audioEffect.getUIntParameter(PARAM_GET_VERSION)
        fun getDisableReason() = audioEffect.getIntParameter(PARAM_GET_DISABLE_REASON)
        fun getConfig() = audioEffect.getByteArrayParameter(PARAM_GET_CONFIG, 40)
        fun getArchitecture() = audioEffect.getUByteParameter(PARAM_GET_ARCHITECTURE)
    }

    inner class MasterLimiter {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun setOutputGain(gainL: UByte, gainR: UByte) = audioEffect.setUByteArrayParameter(
            PARAM_SET_OUTPUT_GAIN, ubyteArrayOf(gainL, gainR))
        fun setThresholdLimit(thresholdLimit: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_THRESHOLD_LIMIT, thresholdLimit)
    }

    inner class PlaybackGainControl {
        // TODO
    }

    inner class FETCompressor {
        // TODO
    }

    inner class ViPERDDC {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_VIPER_DDC_ENABLE, enabled)
        fun setCoefficients(
            coefficients44100: FloatArray,
            coefficients48000: FloatArray,
        ) {
            require(coefficients44100.size == coefficients48000.size) { "Coefficients must be the same size" }

            val array = ByteBuffer.allocate(4 + coefficients44100.size * 4 + coefficients48000.size * 4).order(ByteOrder.nativeOrder())
                .putUInt(coefficients44100.size.toUInt())
                .putFloatArray(coefficients44100)
                .putFloatArray(coefficients48000)
                .array()
            audioEffect.setByteArrayParameter(PARAM_SET_VIPER_DDC_COEFFICIENTS, array)
        }
    }

    inner class SpectrumExtension {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_SPECTRUM_EXTENSION_ENABLE, enabled)
        fun setStrength(strength: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_SPECTRUM_EXTENSION_STRENGTH, strength)
    }

    inner class IIREqualizer {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_IIR_EQUALIZER_ENABLE, enabled)
        fun setBands(bands: UByte) {
            // TODO
        }
        fun setBandLevel(band: UByte, level: Short) {
            val array = ByteBuffer.allocate(3).order(ByteOrder.nativeOrder())
                .putUByte(band)
                .putShort(level)
                .array()
            audioEffect.setByteArrayParameter(PARAM_SET_IIR_EQUALIZER_BAND_LEVEL, array)
        }
    }

    inner class Convolver {
        // TODO
    }

    inner class FieldSurround {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_FIELD_SURROUND_ENABLE, enabled)
        fun setDepth(depth: UShort) = audioEffect.setUShortParameter(PARAM_SET_FIELD_SURROUND_DEPTH, depth)
        fun setMidImage(midImage: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_FIELD_SURROUND_MID_IMAGE, midImage)
    }

    inner class DifferentialSurround {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_DIFFERENTIAL_SURROUND_ENABLE, enabled)
        fun setDelay(delay: UShort) = audioEffect.setUShortParameter(
            PARAM_SET_DIFFERENTIAL_SURROUND_DELAY, delay)
    }

    inner class HeadphoneSurroundPlus {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_HEADPHONE_SURROUND_ENABLE, enabled)
        fun setLevel(level: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_HEADPHONE_SURROUND_LEVEL, level)
    }

    inner class Reverberation {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_REVERBERATION_ENABLE, enabled)
        fun setRoomSize(roomSize: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_REVERBERATION_ROOM_SIZE, roomSize)
        fun setSoundField(soundField: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_REVERBERATION_SOUND_FIELD, soundField)
        fun setDamping(damping: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_REVERBERATION_DAMPING, damping)
        fun setWetSignal(wetSignal: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_REVERBERATION_WET_SIGNAL, wetSignal)
        fun setDrySignal(drySignal: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_REVERBERATION_DRY_SIGNAL, drySignal)
    }

    inner class DynamicSystem {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_DYNAMIC_SYSTEM_ENABLE, enabled)
        fun setXCoefficients(low: UShort, high: UShort) {
            val array = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder())
                .putUShort(low)
                .putUShort(high)
                .array()
            audioEffect.setByteArrayParameter(PARAM_SET_DYNAMIC_SYSTEM_X_COEFFICIENTS, array)
        }
        fun setYCoefficients(low: UShort, high: UShort) {
            val array = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder())
                .putUShort(low)
                .putUShort(high)
                .array()
            audioEffect.setByteArrayParameter(PARAM_SET_DYNAMIC_SYSTEM_Y_COEFFICIENTS, array)
        }
        fun setSideGain(gainX: UByte, gainY: UByte) {
            val array = ByteBuffer.allocate(2).order(ByteOrder.nativeOrder())
                .putUByte(gainX)
                .putUByte(gainY)
                .array()
            audioEffect.setByteArrayParameter(PARAM_SET_DYNAMIC_SYSTEM_SIDE_GAIN, array)
        }
        fun setStrength(strength: UShort) = audioEffect.setUShortParameter(
            PARAM_SET_DYNAMIC_SYSTEM_STRENGTH, strength)
    }

    inner class TubeSimulator {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_TUBE_SIMULATOR_ENABLE, enabled)
    }

    inner class ViPERBass {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_VIPER_BASS_ENABLE, enabled)
        fun setMode(mode: UByte) = audioEffect.setUByteParameter(PARAM_SET_VIPER_BASS_MODE, mode)
        fun setFrequency(frequency: UByte) = audioEffect.setUByteParameter(
            PARAM_SET_VIPER_BASS_FREQUENCY, frequency)
        fun setGain(gain: UShort) = audioEffect.setUShortParameter(PARAM_SET_VIPER_BASS_GAIN, gain)
    }

    inner class ViPERClarity {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_VIPER_CLARITY_ENABLE, enabled)
        fun setMode(mode: UByte) = audioEffect.setUByteParameter(PARAM_SET_VIPER_CLARITY_MODE, mode)
        fun setGain(gain: UShort) = audioEffect.setUShortParameter(PARAM_SET_VIPER_CLARITY_GAIN, gain)
    }

    inner class AuditorySystemProtection {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_CURE_ENABLE, enabled)
        fun setLevel(level: UByte) = audioEffect.setUByteParameter(PARAM_SET_CURE_LEVEL, level)
    }

    inner class AnalogX {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(PARAM_SET_ANALOGX_ENABLE, enabled)
        fun setLevel(level: UByte) = audioEffect.setUByteParameter(PARAM_SET_ANALOGX_LEVEL, level)
    }

    inner class SpeakerOptimization {
        fun setEnabled(enabled: Boolean) = audioEffect.setBooleanParameter(
            PARAM_SET_SPEAKER_OPTIMIZATION_ENABLE, enabled)
    }

    companion object {
        /* UUIDs */
        val VIPER_TYPE_UUID = UUID.fromString("b9bc100c-26cd-42e6-acb6-cad8c3f778de")
        val VIPER_UUID = UUID.fromString("90380da3-8536-4744-a6a3-5731970e640f")

        /* Get parameter */
        private const val PARAM_GET_ENABLED = 0u
        private const val PARAM_GET_FRAME_COUNT = 1u
        private const val PARAM_GET_VERSION = 2u
        private const val PARAM_GET_DISABLE_REASON = 3u
        private const val PARAM_GET_CONFIG = 4u
        private const val PARAM_GET_ARCHITECTURE = 5u

        /* Set parameter */
        private const val PARAM_SET_RESET = 0u
        private const val PARAM_SET_VIPER_DDC_ENABLE = 1u
        private const val PARAM_SET_VIPER_DDC_COEFFICIENTS = 2u
        private const val PARAM_SET_VIPER_BASS_ENABLE = 3u
        private const val PARAM_SET_VIPER_BASS_MODE = 4u
        private const val PARAM_SET_VIPER_BASS_FREQUENCY = 5u
        private const val PARAM_SET_VIPER_BASS_GAIN = 6u
        private const val PARAM_SET_VIPER_CLARITY_ENABLE = 7u
        private const val PARAM_SET_VIPER_CLARITY_MODE = 8u
        private const val PARAM_SET_VIPER_CLARITY_GAIN = 9u
        private const val PARAM_SET_OUTPUT_GAIN = 10u
        private const val PARAM_SET_THRESHOLD_LIMIT = 11u
        private const val PARAM_SET_SPEAKER_OPTIMIZATION_ENABLE = 12u
        private const val PARAM_SET_ANALOGX_ENABLE = 13u
        private const val PARAM_SET_ANALOGX_LEVEL = 14u
        private const val PARAM_SET_TUBE_SIMULATOR_ENABLE = 15u
        private const val PARAM_SET_CURE_ENABLE = 16u
        private const val PARAM_SET_CURE_LEVEL = 17u
        private const val PARAM_SET_REVERBERATION_ENABLE = 18u
        private const val PARAM_SET_REVERBERATION_ROOM_SIZE = 19u
        private const val PARAM_SET_REVERBERATION_SOUND_FIELD = 20u
        private const val PARAM_SET_REVERBERATION_DAMPING = 21u
        private const val PARAM_SET_REVERBERATION_WET_SIGNAL = 22u
        private const val PARAM_SET_REVERBERATION_DRY_SIGNAL = 23u
        private const val PARAM_SET_DIFFERENTIAL_SURROUND_ENABLE = 24u
        private const val PARAM_SET_DIFFERENTIAL_SURROUND_DELAY = 25u
        private const val PARAM_SET_FIELD_SURROUND_ENABLE = 26u
        private const val PARAM_SET_FIELD_SURROUND_DEPTH = 27u
        private const val PARAM_SET_FIELD_SURROUND_MID_IMAGE = 28u
        private const val PARAM_SET_IIR_EQUALIZER_ENABLE = 29u
        private const val PARAM_SET_IIR_EQUALIZER_BAND_LEVEL = 30u
        private const val PARAM_SET_SPECTRUM_EXTENSION_ENABLE = 31u
        private const val PARAM_SET_SPECTRUM_EXTENSION_STRENGTH = 32u
        private const val PARAM_SET_HEADPHONE_SURROUND_ENABLE = 33u
        private const val PARAM_SET_HEADPHONE_SURROUND_LEVEL = 34u
        private const val PARAM_SET_DYNAMIC_SYSTEM_ENABLE = 35u
        private const val PARAM_SET_DYNAMIC_SYSTEM_X_COEFFICIENTS = 36u
        private const val PARAM_SET_DYNAMIC_SYSTEM_Y_COEFFICIENTS = 37u
        private const val PARAM_SET_DYNAMIC_SYSTEM_SIDE_GAIN = 38u
        private const val PARAM_SET_DYNAMIC_SYSTEM_STRENGTH = 39u
    }
}