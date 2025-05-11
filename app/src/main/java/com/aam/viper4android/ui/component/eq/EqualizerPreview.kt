package com.aam.viper4android.ui.component.eq

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.aam.viper4android.ui.util.MinPhaseIirCoeffs
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin

private fun getBandSpacing(size: Size): Float {
    return getBandSpacing(size.width)
}

private fun getBandSpacing(size: IntSize): Float {
    return getBandSpacing(size.width.toFloat())
}

private fun getBandSpacing(width: Float): Float {
    return width / (10 + 1)
}

private fun magnitudeAtFreq(freqHz: Double, sampleRate: Double, coeffs: DoubleArray): Double {
    val omega = 2.0 * PI * freqHz / sampleRate
    val ejw = Complex(cos(-omega), sin(-omega))

    val b0 = coeffs[0]
    val b1 = coeffs[1]
    val a1 = coeffs[2]
    val a0 = coeffs[3]

    val numerator = Complex(b0, 0.0) + Complex(b1, 0.0) * ejw
    val denominator = Complex(a0, 0.0) + Complex(a1, 0.0) * ejw

    return (numerator / denominator).abs()
}

@Composable
fun EqualizerPreview(
    modifier: Modifier = Modifier,
    gains: List<Float>
) {
    val coeffs = remember(gains.size) { MinPhaseIirCoeffs(when (gains.size) {
        10 -> MinPhaseIirCoeffs.Bands.BAND_10
        15 -> MinPhaseIirCoeffs.Bands.BAND_15
        31 -> MinPhaseIirCoeffs.Bands.BAND_31
        else -> throw IllegalArgumentException("Unsupported number of bands: ${gains.size}")
    }, 44100).getCoeffs() }
    var boxSize by remember { mutableStateOf(Size(0f, 0f)) }
    val bandSpacing = remember(boxSize) { getBandSpacing(boxSize) }
    val innerWidth = remember(bandSpacing, gains) { bandSpacing * (gains.size + 1) }

    // Theme
    val labelSmall = MaterialTheme.typography.labelSmall
    val primary = MaterialTheme.colorScheme.primary
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = modifier
            .onSizeChanged { newSize ->
                boxSize = newSize.toSize()
            }
            .horizontalScroll(rememberScrollState())
    ) {
        val textMeasurer = rememberTextMeasurer()

        val bands = remember(gains) {
            when (gains.size) {
                10 -> EqualizerHelper.FREQ_10_BAND
                15 -> EqualizerHelper.FREQ_15_BAND
                31 -> EqualizerHelper.FREQ_31_BAND
                else -> throw IllegalArgumentException("Unsupported number of bands: ${gains.size}")
            }
        }

        Canvas(
            modifier = modifier
                .width(with(LocalDensity.current) { innerWidth.toDp() })
                .fillMaxHeight()
        ) {
            // draw horizontal lines
            val lines = 7 // hardcoded for now at 7 lines
            for (i in 0 until lines) {
                val y = size.height * (i + 1) / (lines + 1)
                drawLine(
                    color = outlineVariant,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val frequencyResponse = Path()
//            EqualizerHelper.calculateFrequencyResponse(
//                size = size,
//                gains = gains,
//                frequencyResponse = frequencyResponse
//            )

            // gradient
            drawPath(
                path = Path().apply {
                    addPath(frequencyResponse)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                },
                brush = Brush.verticalGradient(
                    0f to primary,
                    1f to Color.Transparent,
                ),
                alpha = 0.75f
            )

            // eq line
            drawPath(
                path = frequencyResponse,
                color = primary,
                style = Stroke(
                    width = 2.dp.toPx(),
                )
            )

            // top and bottom text
            for (i in gains.indices) {
                val x = bandSpacing * (i + 1)

                // top text
                val topTextLayoutResult = textMeasurer.measure(
                    text = "%.1f".format(gains[i]),
                    style = labelSmall
                )
                drawText(
                    textLayoutResult = topTextLayoutResult,
                    color = onSurfaceVariant,
                    topLeft = Offset(
                        x - topTextLayoutResult.size.width / 2,
                        8.dp.toPx()
                    )
                )

                // bottom text
                val bottomTextLayoutResult = textMeasurer.measure(
                    text = EqualizerHelper.getBandText(bands[i]),
                    style = labelSmall
                )
                drawText(
                    textLayoutResult = bottomTextLayoutResult,
                    color = onSurfaceVariant,
                    topLeft = Offset(
                        x - bottomTextLayoutResult.size.width / 2,
                        size.height - 8.dp.toPx() - bottomTextLayoutResult.size.height
                    )
                )
            }
        }
    }

    fun xToFrequency(x: Int, width: Int, minFreq: Double, maxFreq: Double): Double {
        val logMin = log10(minFreq)
        val logMax = log10(maxFreq)
        val logFreq = logMin + (x.toDouble() / (width - 1)) * (logMax - logMin)
        return 10.0.pow(logFreq)
    }

    fun frequencyToX(freq: Double, width: Int, samplingRate: Int): Int {
        val minFreq = 20.0
        val maxFreq = samplingRate / 2.0
        val logMin = log10(minFreq)
        val logMax = log10(maxFreq)
        val logFreq = log10(freq)
        return ((width - 1) * (logFreq - logMin) / (logMax - logMin)).toInt()
    }

    fun calculateFrequencyResponse(
        frequencies: List<Double>,
        gains: List<Float>,
        width: Int,
        samplingRate: Int
    ): FloatArray {
        require(frequencies.size == gains.size) { "Frequencies and gains must have same size" }

        val response = FloatArray(width)
        val minFreq = frequencies.first()
        val maxFreq = frequencies.last()

        // Create interpolation points (frequency, gain) pairs
        val points = frequencies.mapIndexed { index, freq ->
            freq to gains[index].toDouble()
        }

        // For each pixel column (frequency point)
        for (x in 0 until width) {
            val freq = xToFrequency(x, width, minFreq, maxFreq)

            // Find the two nearest bands for interpolation
            var lowerIndex = 0
            var upperIndex = frequencies.size - 1

            for (i in frequencies.indices) {
                if (frequencies[i] <= freq) lowerIndex = i
                if (frequencies[i] >= freq) {
                    upperIndex = i
                    break
                }
            }

            // Linear interpolation between bands
            val db = if (lowerIndex == upperIndex) {
                gains[lowerIndex].toDouble()
            } else {
                val lowerFreq = frequencies[lowerIndex]
                val upperFreq = frequencies[upperIndex]
                val lowerGain = gains[lowerIndex].toDouble()
                val upperGain = gains[upperIndex].toDouble()

                val alpha = (log10(freq) - log10(lowerFreq)) /
                        (log10(upperFreq) - log10(lowerFreq))
                lowerGain * (1 - alpha) + upperGain * alpha
            }

            response[x] = db.toFloat()
        }

        return response
    }
}

@Preview
@Composable
private fun EqualizerPreviewPreview() {
    Surface {
        EqualizerPreview(
            modifier = Modifier.height(250.dp).fillMaxWidth(),
            gains = listOf(
                10.0f, -1.0f, 8.0f, 6.0f, -9.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            )
        )
    }
}