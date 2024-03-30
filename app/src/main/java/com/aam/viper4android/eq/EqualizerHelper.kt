package com.aam.viper4android.eq

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
    const val MIN_DB = -12
    const val MAX_DB = 12
    const val MIN_FREQ = 22.0
    const val MAX_FREQ = 24000.0
    const val SAMPLING_RATE = MAX_FREQ * 2

    fun calculateFrequencyResponse(
        biquads: Array<Biquad>,
        size: Size,
        gains: FloatArray,
        frequencyResponse: Path
    ) {
        /*
         * The filtering is realized with 2nd order high shelf filters, and each
         * band is realized as a transition relative to the previous band. The
         * center point for each filter is actually between the bands. 1st band
         * has no previous band, so it's just a fixed gain.
         */
        val gain = 10.0.pow(gains[0] / 20.0)
        for (i in biquads.indices) {
            val frequency = 15.625 * 2.0.pow(i + 0.5)
            biquads[i].setHighShelf(frequency * 2, gains[i + 1] - gains[i])
        }

        for (i in 0 until 129) {
            val frequency = reverseProjectX(i / 127.0)
            val omega = frequency / EqualizerHelper.SAMPLING_RATE * Math.PI * 2
            val z0 = getComplex(cos(omega), sin(omega))

            /* Evaluate the response at frequency z0 */

            /* val z1 = z0.mul(gain) */
            val z2 = biquads[0].evaluateTransfer(z0)
            val z3 = biquads[1].evaluateTransfer(z0)
            val z4 = biquads[2].evaluateTransfer(z0)
            val z5 = biquads[3].evaluateTransfer(z0)
            val z6 = biquads[4].evaluateTransfer(z0)
            val z7 = biquads[5].evaluateTransfer(z0)
            val z8 = biquads[6].evaluateTransfer(z0)
            val z9 = biquads[7].evaluateTransfer(z0)
            val z10 = biquads[8].evaluateTransfer(z0)

            /* Magnitude response, dB */
            val decibel = lin2dB(
                gain * z2.rho() * z3.rho() * z4.rho() * z5.rho() * z6.rho() * z7.rho() * z8.rho() * z9.rho() * z10.rho()
            )
            val x = projectX(frequency) * size.width
            val y = projectY(decibel) * size.height

            if (i == 0) frequencyResponse.moveTo(x, y) else frequencyResponse.lineTo(x, y)
        }
    }

    private fun getComplex(cosine: Double, sine: Double): Complex {
        return Complex(cosine, sine)
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