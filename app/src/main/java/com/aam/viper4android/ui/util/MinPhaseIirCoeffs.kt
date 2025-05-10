package com.aam.viper4android.ui.util

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
    fun getCoeffs(): List<List<Double>> {
        val coeffs = mutableListOf<List<Double>>()
        for (frequency in bands.frequencies) {
            val normalizedFrequency = frequency / (samplingRate / 2.0)
            val coeff = DoubleArray(4)
            coeff[0] = 1.0
            coeff[1] = -2.0 * Math.cos(2.0 * Math.PI * normalizedFrequency)
            coeff[2] = 1.0
            coeff[3] = -Math.exp(-2.0 * Math.PI * normalizedFrequency)
            coeffs.add(coeff.toList())
        }
        return coeffs
    }

    enum class Bands(
        val frequencies: List<Double>,
    ) {
        BAND_10(BAND_10_FREQUENCIES),
        BAND_15(BAND_15_FREQUENCIES),
        BAND_31(BAND_31_FREQUENCIES),
    }
}