package com.aam.viper4android.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aam.viper4android.R
import com.aam.viper4android.ui.component.slider.WSTSlider
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValueSlider(
    title: String,
    summary: String? = null,
    summaryUnit: String = "",
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: ClosedRange<Int> = 0..1,
    steps: Int = 0
) {
    Column(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = (summary ?: value.toString()) + summaryUnit,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    letterSpacing = 0.25.sp
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(R.drawable.ic_restart),
                    contentDescription = "Reset to default"
                )
            }
        }
        WSTSlider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            modifier = Modifier.padding(horizontal = 21.dp),
            steps = steps,
            valueRange = valueRange.let { it.start.toFloat()..it.endInclusive.toFloat() }
        )
    }
}

@Composable
fun ValueSlider(
    title: String,
    summary: String? = null,
    summaryUnit: String = "",
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
) {
    Column {
        Text(text = title)
        Text(text = (summary ?: value.toString()) + summaryUnit)
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange, steps = steps)
    }
}

@Preview
@Composable
private fun ValueSliderPreview() {
    Surface {
        ValueSlider(
            title = "Volume",
            summary = "50",
            summaryUnit = "%",
            value = 50,
            onValueChange = {},
            valueRange = 0..100
        )
    }
}