package com.aam.viper4android.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.aam.viper4android.eq.EqualizerHelper
import com.aam.viper4android.view.Biquad
import java.util.Locale

private val text10Band = arrayOf(
    "31",
    "62",
    "125",
    "250",
    "500",
    "1k",
    "2k",
    "4k",
    "8k",
    "16k",
)

private val text15Band = arrayOf(
    "25",
    "40",
    "63",
    "100",
    "160",
    "250",
    "400",
    "630",
    "1k",
    "1.6k",
    "2.5k",
    "4k",
    "6.3k",
    "10k",
    "16k",
)

private val text25Band = arrayOf(
    "20",
    "31.5",
    "40",
    "50",
    "80",
    "100",
    "125",
    "160",
    "250",
    "315",
    "400",
    "500",
    "800",
    "1k",
    "1.25k",
    "1.6k",
    "2.5k",
    "3.15k",
    "4k",
    "5k",
    "8k",
    "10k",
    "12.5k",
    "16k",
    "20k",
)

private val text31Band = arrayOf(
    "20",
    "25",
    "31.5",
    "40",
    "50",
    "63",
    "80",
    "100",
    "125",
    "160",
    "200",
    "250",
    "315",
    "400",
    "500",
    "630",
    "800",
    "1k",
    "1.25k",
    "1.6k",
    "2k",
    "2.5k",
    "3.15k",
    "4k",
    "5k",
    "6.3k",
    "8k",
    "10k",
    "12.5k",
    "16k",
    "20k",
)

private fun getBandSpacing(size: Size): Float {
    return getBandSpacing(size.width)
}

private fun getBandSpacing(size: IntSize): Float {
    return getBandSpacing(size.width.toFloat())
}

private fun getBandSpacing(width: Float): Float {
    return width / (10 + 1)
}

@Composable
fun EqualizerPreview(
    modifier: Modifier = Modifier,
    gains: FloatArray
) {
    val biquads = remember { Array(9) { Biquad() } }
    val textMeasurer = rememberTextMeasurer()

    val bands = remember(gains) { gains.size }
    val bandText = remember(bands) {
        when (bands) {
            10 -> text10Band
            15 -> text15Band
            25 -> text25Band
            31 -> text31Band
            else -> throw IllegalArgumentException("Unsupported number of bands: $bands")
        }
    }

    val labelSmall = MaterialTheme.typography.labelSmall
    val primary = MaterialTheme.colorScheme.primary
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant

    Canvas(
        modifier = modifier.height(250.dp).fillMaxWidth()
    ) {
        val bandSpacing = getBandSpacing(size)

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
        EqualizerHelper.calculateFrequencyResponse(
            biquads = biquads,
            size = size,
            gains = gains,
            frequencyResponse = frequencyResponse
        )

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
        for (i in 0 until bands) {
            val x = bandSpacing * (i + 1)

            // top text
            val topTextLayoutResult = textMeasurer.measure(
                text = String.format(Locale.ROOT, "%.1f", gains[i]),
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
                text = bandText[i],
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

@Preview
@Composable
private fun EqualizerPreviewPreview() {
    Surface {
        EqualizerPreview(
            gains = floatArrayOf(
                10.0f, -1.0f, 8.0f, 6.0f, -9.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
            )
        )
    }
}