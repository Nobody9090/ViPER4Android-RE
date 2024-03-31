package com.aam.viper4android.ui.component

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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.aam.viper4android.ui.component.eq.EqualizerHelper
import java.util.Locale
import kotlin.math.min
import kotlin.math.roundToInt

private fun getBandSpacing(size: IntSize): Float {
    return size.width.toFloat() / (10 + 1)
}

@Composable
fun EqualizerEditor(
    modifier: Modifier = Modifier,
    gains: List<Float>,
    onGainsChanged: (List<Float>) -> Unit
) {
    // Theme constants
    val labelSmall = MaterialTheme.typography.labelSmall
    val primary = MaterialTheme.colorScheme.primary
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant

    val density = LocalDensity.current

    val currentGains by rememberUpdatedState(gains)
    var boxSize by remember { mutableStateOf(IntSize.Zero) }
    val currentBoxSize by rememberUpdatedState(boxSize)
    val bandSpacing = remember(boxSize) { getBandSpacing(boxSize) }
    val currentBandSpacing by rememberUpdatedState(bandSpacing)
    val innerWidth = remember(bandSpacing, gains) { bandSpacing * (gains.size + 1) }

    val textMeasurer = rememberTextMeasurer()

    val bands = remember(gains) {
        when (gains.size) {
            10 -> EqualizerHelper.FREQ_10_BAND
            15 -> EqualizerHelper.FREQ_15_BAND
            25 -> EqualizerHelper.FREQ_25_BAND
            31 -> EqualizerHelper.FREQ_31_BAND
            else -> throw IllegalArgumentException("Unsupported number of bands: ${gains.size}")
        }
    }

    val activeSliders = remember { mutableMapOf<PointerId, Int>() }
    val px48dp = remember(density) { with(density) { 48.dp.toPx() } }
    val currentPx48dp by rememberUpdatedState(px48dp)

    fun getGainAtY(y: Float): Float {
        val gain = (EqualizerHelper.MAX_DB - y / currentBoxSize.height * (EqualizerHelper.MAX_DB - EqualizerHelper.MIN_DB)).coerceIn(
            EqualizerHelper.MIN_DB,
            EqualizerHelper.MAX_DB
        )
        return (gain * 10).roundToInt() / 10f
    }

    fun findBandIndex(offset: Offset): Int? {
        return currentGains.indices.firstOrNull {
            val x = currentBandSpacing * (it + 1)
            val y = (EqualizerHelper.MAX_DB - currentGains[it]) / (EqualizerHelper.MAX_DB - EqualizerHelper.MIN_DB) * currentBoxSize.height
            val radius = min(currentPx48dp / 2, currentBandSpacing / 2)
            val matchesX = offset.x >= x - radius && offset.x <= x + radius
            val matchesY = offset.y >= y - radius && offset.y <= y + radius
            matchesX && matchesY
        }
    }

    Box(
        modifier = modifier
            .onSizeChanged { newSize -> boxSize = newSize }
            .horizontalScroll(rememberScrollState())
    ) {
        Canvas(
            modifier = modifier
                .width(with(density) { innerWidth.toDp() })
                .fillMaxHeight()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            when (event.type) {
                                PointerEventType.Press -> {
                                    event.changes.forEach { change ->
                                        val index = findBandIndex(change.position)
                                        if (index != null) {
                                            change.consume()
                                            activeSliders[change.id] = index
                                        }
                                    }
                                }
                                PointerEventType.Move -> {
                                    event.changes.forEach { change ->
                                        val index = activeSliders[change.id]
                                        if (index != null) {
                                            change.consume()
                                            val newGain = getGainAtY(change.position.y)
                                            if (newGain != currentGains[index]) {
                                                onGainsChanged(
                                                    currentGains.mapIndexed { i, gain ->
                                                        if (i == index) newGain else gain
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                                PointerEventType.Release -> {
                                    event.changes.forEach { change ->
                                        if (activeSliders.remove(change.id) != null) {
                                            change.consume()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
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
            EqualizerHelper.calculateFrequencyResponse(
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
            for (i in gains.indices) {
                val x = bandSpacing * (i + 1)
                val y = (EqualizerHelper.MAX_DB - gains[i]) / (EqualizerHelper.MAX_DB - EqualizerHelper.MIN_DB) * boxSize.height

                drawCircle(
                    color = primary,
                    center = Offset(x, y),
                    radius = 9.dp.toPx()
                )

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
}

@Preview
@Composable
private fun EqualizerEditorPreview() {
    Surface {
        EqualizerEditor(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(),
            gains = listOf(
                0.0f, -1.0f, -2.0f, -3.0f, -2.0f, -1.0f, 0.0f, 1.0f, 0.0f, -1.0f
            ),
            onGainsChanged = {  }
        )
    }
}