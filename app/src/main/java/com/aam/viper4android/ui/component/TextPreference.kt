package com.aam.viper4android.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextPreference(
    modifier: Modifier = Modifier,
    icon: @Composable (() -> Unit)? = null,
    title: String,
    summary: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            icon()
        }
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp
            )
            if (summary != null) {
                Text(
                    text = summary,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    letterSpacing = 0.25.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
private fun TextPreferencePreview() {
    Surface {
        TextPreference(
            title = "Title"
        )
    }
}

@Preview
@Composable
private fun TextPreferenceWithSummaryPreview() {
    Surface {
        TextPreference(
            title = "Title",
            summary = "Summary"
        )
    }
}

@Preview
@Composable
private fun TextPreferenceWithIconPreview() {
    Surface {
        TextPreference(
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            title = "Title"
        )
    }
}