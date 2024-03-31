package com.aam.viper4android.ui.component.eq

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import com.aam.viper4android.view.Biquad
import com.aam.viper4android.view.Complex
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sin

object EqualizerHelper {
    const val MIN_DB = -12f
    const val MAX_DB = 12f
    const val MIN_FREQ = 22f
    const val MAX_FREQ = 24000f
    const val SAMPLING_RATE = MAX_FREQ * 2

    val FREQ_10_BAND = arrayOf(
        31.0, 62.0, 125.0, 250.0, 500.0, 1000.0, 2000.0, 4000.0, 8000.0, 16000.0
    )
    val FREQ_15_BAND = arrayOf(
        25.0,
        40.0,
        63.0,
        100.0,
        160.0,
        250.0,
        400.0,
        630.0,
        1000.0,
        1600.0,
        2500.0,
        4000.0,
        6300.0,
        10000.0,
        16000.0
    )
    val FREQ_25_BAND = arrayOf(
        20.0,
        31.5,
        40.0,
        50.0,
        80.0,
        100.0,
        125.0,
        160.0,
        250.0,
        315.0,
        400.0,
        500.0,
        800.0,
        1000.0,
        1250.0,
        1600.0,
        2500.0,
        3150.0,
        4000.0,
        5000.0,
        8000.0,
        10000.0,
        12500.0,
        16000.0,
        20000.0
    )
    val FREQ_31_BAND = arrayOf(
        20.0,
        25.0,
        31.5,
        40.0,
        50.0,
        63.0,
        80.0,
        100.0,
        125.0,
        160.0,
        200.0,
        250.0,
        315.0,
        400.0,
        500.0,
        630.0,
        800.0,
        1000.0,
        1250.0,
        1600.0,
        2000.0,
        2500.0,
        3150.0,
        4000.0,
        5000.0,
        6300.0,
        8000.0,
        10000.0,
        12500.0,
        16000.0,
        20000.0
    )

    fun getBandText(band: Double): String {
        return if (band < 1000) {
            if (band % 1 == 0.0) "%.0f".format(band) else "%.1f".format(band)
        } else {
            val scaledBand = band / 1000
            if (scaledBand % 1 == 0.0) "%.0fk".format(scaledBand) else "%.1fk".format(scaledBand)
        }
    }

    fun calculateFrequencyResponse(
        size: Size, gains: List<Float>, frequencyResponse: Path
    ) {
        val bands = when (gains.size) {
            10 -> FREQ_10_BAND
            15 -> FREQ_15_BAND
            25 -> FREQ_25_BAND
            31 -> FREQ_31_BAND
            else -> throw IllegalArgumentException("Unsupported number of bands: ${gains.size}")
        }
        val biquads = Array(gains.size - 1) { index ->
            Biquad().apply {
                setHighShelf(bands[index], gains[index + 1] - gains[index])
            }
        }

        /*
         * The filtering is realized with 2nd order high shelf filters, and each
         * band is realized as a transition relative to the previous band. The
         * center point for each filter is actually between the bands. 1st band
         * has no previous band, so it's just a fixed gain.
         */
        val gain = 10.0.pow(gains[0] / 20.0)
        for (i in 0 until 129) {
            val frequency = reverseProjectX(i / 127.0)
            val omega = frequency / SAMPLING_RATE * Math.PI * 2
            val z0 = Complex(cos(omega), sin(omega))

            /* Evaluate the response at frequency z0 */

            /* val z1 = z0.mul(gain) */
            var tmp = gain
            for (biquad in biquads) {
                tmp *= biquad.evaluateTransfer(z0).rho()
            }

            /* Magnitude response, dB */
            val decibel = lin2dB(tmp)
            val x = projectX(frequency) * size.width
            val y = projectY(decibel) * size.height

            if (i == 0) frequencyResponse.moveTo(x, y) else frequencyResponse.lineTo(x, y)
        }
    }

    private fun reverseProjectX(position: Double): Double {
        val minimumPosition = ln(MIN_FREQ)
        val maximumPosition = ln(MAX_FREQ)
        return exp(position * (maximumPosition - minimumPosition) + minimumPosition)
    }

    private fun projectX(frequency: Double): Float {
        val position = ln(frequency)
        val minimumPosition = ln(MIN_FREQ)
        val maximumPosition = ln(MAX_FREQ)
        return ((position - minimumPosition) / (maximumPosition - minimumPosition)).toFloat()
    }

    private fun projectY(dB: Float): Float {
        val pos = (dB - MIN_DB) / (MAX_DB - MIN_DB)
        return 1 - pos
    }

    private fun lin2dB(rho: Double) =
        if (rho != 0.0) (ln(rho) / ln(10.0) * 20).toFloat() else -99.9f
}