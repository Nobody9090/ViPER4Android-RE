package com.aam.viper4android.view

import com.aam.viper4android.ui.component.eq.EqualizerHelper
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class Biquad {
    private lateinit var mB0: Complex
    private lateinit var mB1: Complex
    private lateinit var mB2: Complex
    private lateinit var mA0: Complex
    private lateinit var mA1: Complex
    private lateinit var mA2: Complex

    fun setHighShelf(centerFrequency: Double, dbGain: Float) {
        val w0: Double = 2 * Math.PI * centerFrequency / EqualizerHelper.SAMPLING_RATE
        val a = 10.0.pow(dbGain / 40.0)
        val alpha = sin(w0) / 2 * sqrt(2.0)

        mB0 = Complex(a * (a + 1.0 + (a - 1) * cos(w0) + 2.0 * sqrt(a) * alpha), 0.0)
        mB1 = Complex(-2.0 * a * (a - 1 + (a + 1) * cos(w0)), 0.0)
        mB2 = Complex(a * (a + 1.0 + (a - 1) * cos(w0) - 2.0 * sqrt(a) * alpha), 0.0)
        mA0 = Complex(a + 1 - (a - 1) * cos(w0) + 2.0 * sqrt(a) * alpha, 0.0)
        mA1 = Complex(2 * (a - 1.0 - (a + 1) * cos(w0)), 0.0)
        mA2 = Complex(a + 1 - (a - 1) * cos(w0) - 2.0 * sqrt(a) * alpha, 0.0)
    }

    fun evaluateTransfer(z: Complex): Complex {
        val zSquared = z.mul(z)
        val nom = mB0.add(mB1.div(z)).add(mB2.div(zSquared))
        val den = mA0.add(mA1.div(z)).add(mA2.div(zSquared))
        return nom.div(den)
    }
}