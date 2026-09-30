package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppSlider  (single-thumb continuous or stepped slider)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A single-thumb slider.
 *
 * @param value               Current thumb position (must be within [valueRange]).
 * @param onValueChange       Called continuously while the user drags.
 * @param modifier            Layout modifier applied to the slider.
 * @param enabled             When false, the slider is dimmed and locked.
 * @param valueRange          Min..Max range — defaults to 0f..1f.
 * @param steps               0 = continuous; N = N + 1 discrete stops.
 * @param onValueChangeFinished Called once when the user lifts their finger.
 * @param colors              Override thumb, track, tick colors.
 * @param label               Optional label composable shown above the slider.
 * @param valueLabel          Optional value display composable shown to the right.
 */
@Composable
fun AppSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(),
    label: @Composable (() -> Unit)? = null,
    valueLabel: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (label != null || valueLabel != null) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                if (label != null) label()
                if (label != null && valueLabel != null) Spacer(Modifier.width(8.dp))
                if (valueLabel != null) valueLabel()
            }
        }
        Slider(
            value                 = value,
            onValueChange         = onValueChange,
            modifier              = Modifier.fillMaxWidth(),
            enabled               = enabled,
            valueRange            = valueRange,
            steps                 = steps,
            onValueChangeFinished = onValueChangeFinished,
            colors                = colors,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppRangeSlider  (two-thumb slider for selecting a range)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A range slider with two thumbs (start and end).
 *
 * Usage:
 * ```
 * var range by remember { mutableStateOf(20f..80f) }
 * AppRangeSlider(
 *     value         = range,
 *     onValueChange = { range = it },
 *     valueRange    = 0f..100f,
 *     label         = { Text("Price Range") },
 *     valueLabel    = {
 *         Text("${range.start.toInt()} – ${range.endInclusive.toInt()} $")
 *     }
 * )
 * ```
 *
 * @param value               Current [ClosedFloatingPointRange] (start..end).
 * @param onValueChange       Called continuously while the user drags either thumb.
 * @param valueRange          Min..Max of the full range.
 * @param steps               0 = continuous; N = N + 1 discrete stops.
 * @param onValueChangeFinished Called once when user lifts a finger.
 */
@Composable
fun AppRangeSlider(
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: SliderColors = SliderDefaults.colors(),
    label: @Composable (() -> Unit)? = null,
    valueLabel: @Composable (() -> Unit)? = null,
) {
    Column(modifier = modifier) {
        if (label != null || valueLabel != null) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                if (label != null) label()
                if (valueLabel != null) valueLabel()
            }
        }
        RangeSlider(
            value                 = value,
            onValueChange         = onValueChange,
            modifier              = Modifier.fillMaxWidth(),
            enabled               = enabled,
            valueRange            = valueRange,
            steps                 = steps,
            onValueChangeFinished = onValueChangeFinished,
            colors                = colors,
        )
        // Range bounds hint
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text  = valueRange.start.toInt().toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text  = valueRange.endInclusive.toInt().toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
