package com.aam.viper4android

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class Session(
    private val viperManager: ViPERManager,
    val packageName: String,
    val sessionId: Int,
) {
    val effect = ViPEREffect(sessionId)
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        if (!effect.audioEffect.hasControl()) {
            release()
            throw IllegalStateException("Failed to get control of audio effect")
        }

        collectEffects()
    }

    private fun collectEffects() {
        collect(viperManager.enabled, effect.audioEffect::setEnabled)
        collect(viperManager.analogX.enabled, effect.analogX::setEnabled)
        collect(viperManager.auditorySystemProtection.enabled, effect.auditorySystemProtection::setEnabled)
        collect(viperManager.differentialSurround.enabled, effect.differentialSurround::setEnabled)
        collect(viperManager.differentialSurround.delay) {
            effect.differentialSurround.setDelay(it.toUShort())
        }
        collect(viperManager.firEqualizer.enabled, effect.iirEqualizer::setEnabled)
        collect(viperManager.headphoneSurroundPlus.enabled, effect.headphoneSurroundPlus::setEnabled)
        collect(viperManager.headphoneSurroundPlus.level) {
            effect.headphoneSurroundPlus.setLevel(it.toUByte())
        }

        collect(
            viperManager.masterLimiter.outputGain
                .combine(viperManager.masterLimiter.outputPan) { gain, pan ->
                    gain to pan
                }
        ) { (gain, pan) ->
            val panL = if (pan < 50) 1.0f else ((100.0f - pan.toFloat()) / 50.0f)
            val panR = if (pan > 50) 1.0f else (pan.toFloat() / 50.0f)
            val gainL = (gain.toFloat() * panL).roundToInt().toUByte()
            val gainR = (gain.toFloat() * panR).roundToInt().toUByte()
            effect.masterLimiter.setOutputGain(gainL, gainR)
        }

        collect(viperManager.masterLimiter.thresholdLimit) {
            effect.masterLimiter.setThresholdLimit(it.toUByte())
        }
        collect(viperManager.reverberation.enabled, effect.reverberation::setEnabled)
        collect(viperManager.reverberation.roomSize) {
            effect.reverberation.setRoomSize(it.toUByte())
        }
        collect(viperManager.reverberation.soundField) {
            effect.reverberation.setSoundField(it.toUByte())
        }
        collect(viperManager.reverberation.damping) {
            effect.reverberation.setDamping(it.toUByte())
        }
        collect(viperManager.reverberation.wetSignal) {
            effect.reverberation.setWetSignal(it.toUByte())
        }
        collect(viperManager.reverberation.drySignal) {
            effect.reverberation.setDrySignal(it.toUByte())
        }
        collect(viperManager.speakerOptimization.enabled, effect.speakerOptimization::setEnabled)
        collect(viperManager.spectrumExtension.enabled, effect.spectrumExtension::setEnabled)
        collect(viperManager.spectrumExtension.strength) {
            effect.spectrumExtension.setStrength(it.toUByte())
        }
        collect(viperManager.tubeSimulator6N1J.enabled, effect.tubeSimulator::setEnabled)
        collect(viperManager.viperBass.enabled, effect.viperBass::setEnabled)
        collect(viperManager.viperBass.mode) {
            effect.viperBass.setMode(it.toUByte())
        }
        collect(viperManager.viperBass.frequency) {
            effect.viperBass.setFrequency(it.toUByte())
        }
        collect(viperManager.viperBass.gain) {
            effect.viperBass.setGain((it * 50).toUShort())
        }
        collect(viperManager.viperClarity.enabled, effect.viperClarity::setEnabled)
        collect(viperManager.viperClarity.mode) {
            effect.viperClarity.setMode(it.toUByte())
        }
        collect(viperManager.viperClarity.gain) {
            effect.viperClarity.setGain((it * 50).toUShort())
        }
    }

    private fun <T> collect(
        flow: Flow<T>,
        setter: (T) -> Unit
    ) {
        scope.launch {
            flow.collect {
                setter(it)
            }
        }
    }

    fun release() {
        scope.cancel()
        effect.audioEffect.release()
    }
}