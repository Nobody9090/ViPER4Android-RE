package com.aam.viper4android.ui.util

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private val BAND_10_FREQUENCIES = listOf(
    31.5,
    62.0,
    125.0,
    250.0,
    500.0,
    1000.0,
    2000.0,
    4000.0,
    8000.0,
    16000.0,
)

private val BAND_15_FREQUENCIES = listOf(
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
    16000.0,
)

private val BAND_31_FREQUENCIES = listOf(
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
    20000.0,
)

class MinPhaseIirCoeffs(
    val bands: Bands,
    val samplingRate: Int,
) {
    fun getCoeffs(): List<DoubleArray> {
        val bandwidthOctaves = when (bands) {
            Bands.BAND_10 -> 3.0 / 3.0
            Bands.BAND_15 -> 2.0 / 3.0
            Bands.BAND_31 -> 1.0 / 3.0
        }

        val coeffs = mutableListOf<DoubleArray>()
        for (frequency in bands.frequencies) {
            // Calculate band edges
            val (lowerEdge, upperEdge) = calculateBandEdges(
                frequency,
                bandwidthOctaves
            )

            // Convert to radians
            val centerRadians = 2.0 * PI * frequency / samplingRate
            val lowerRadians = 2.0 * PI * lowerEdge / samplingRate

            // Trigonometric terms
            val cosCenter = cos(centerRadians)
            val cosLower = cos(lowerRadians)
            val sinLower = sin(lowerRadians)
            val cosCenterSq = cosCenter * cosCenter
            val cosLowerSq = cosLower * cosLower

            // Intermediate terms
            val a = cosCenter * cosLower
            val b = cosCenterSq / 2.0
            val c = sinLower * sinLower

            // Quadratic coefficients
            val coeffA = (b - a) + (0.5 - c)
            val coeffB = c + (b + cosLowerSq - a - 0.5)
            val coeffC = 0.125 * (cosCenterSq + 1.0) - 0.25 * (a + c)

            // Solve and store coefficients
            val root = solveQuadratic(coeffA, coeffB, coeffC)
            if (root != null) {
                val bandCoeffs = DoubleArray(4).apply {
                    this[0] = root * 2.0 // b0
                    this[1] = 0.5 - root // b1
                    this[2] = (root + 0.5) * cosCenter * 2.0 // a1
                    this[3] = 1.0 // a0
                }
                coeffs.add(bandCoeffs)
            }
        }
        return coeffs
    }

    private fun calculateBandEdges(centerFreq: Double, bandwidthOctaves: Double): Pair<Double, Double> {
        val factor = 2.0.pow(bandwidthOctaves / 2.0)
        val lowerEdge = centerFreq / factor
        val upperEdge = centerFreq * factor
        return Pair(lowerEdge, upperEdge)
    }

    private fun solveQuadratic(a: Double, b: Double, c: Double): Double? {
        val discriminant = (b * b) / (4.0 * a * a) - (c / a)

        if (discriminant >= 0.0) {
            return null // No real solution
        }

        val sqrtDiscriminant = sqrt(-discriminant)
        val solution1 = -b / (2.0 * a) - sqrtDiscriminant
        val solution2 = -b / (2.0 * a) + sqrtDiscriminant

        // Return the more stable solution (smaller absolute value)
        return if (abs(solution1) < abs(solution2)) solution1 else solution2
    }

    enum class Bands(
        val frequencies: List<Double>,
    ) {
        BAND_10(BAND_10_FREQUENCIES),
        BAND_15(BAND_15_FREQUENCIES),
        BAND_31(BAND_31_FREQUENCIES),
    }
}