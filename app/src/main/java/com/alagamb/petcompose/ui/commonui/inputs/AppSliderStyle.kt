package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppSliderStyle — reusable style for AppSlider / AppRangeSlider
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppSlider(value = vol, onValueChange = { vol = it },
//       colors = AppSliderStyles.success().colors)
//   AppRangeSlider(value = range, onValueChange = { range = it },
//       colors = AppSliderStyles.default().colors)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppSlider] and [AppRangeSlider].
 *
 * @param colors    Active track, inactive track, thumb, tick colors.
 * @param thumbSize Optional thumb size hint (informational — not enforced by Slider itself).
 */
@Stable
data class AppSliderStyle(
    val colors: SliderColors,
    val thumbSize: Dp = 20.dp,
) {
    companion object {
        /**
         * Factory — create a fully custom slider style.
         * Only specify what you want to override.
         */
        @Composable
        fun create(
            thumbColor: Color = MaterialTheme.colorScheme.primary,
            activeTrackColor: Color = MaterialTheme.colorScheme.primary,
            activeTickColor: Color = MaterialTheme.colorScheme.onPrimary,
            inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
            inactiveTickColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledThumbColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledActiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledInactiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thumbSize: Dp = 20.dp,
        ) = AppSliderStyle(
            colors = SliderDefaults.colors(
                thumbColor                = thumbColor,
                activeTrackColor          = activeTrackColor,
                activeTickColor           = activeTickColor,
                inactiveTrackColor        = inactiveTrackColor,
                inactiveTickColor         = inactiveTickColor,
                disabledThumbColor        = disabledThumbColor,
                disabledActiveTrackColor  = disabledActiveTrackColor,
                disabledInactiveTrackColor = disabledInactiveTrackColor,
            ),
            thumbSize = thumbSize,
        )
    }
}

// ── Pre-built Slider styles ────────────────────────────────────────────────

object AppSliderStyles {

    /** Default M3 slider — follows theme primary color. */
    @Composable fun default() = AppSliderStyle(
        colors = SliderDefaults.colors(),
    )

    /** Success / green slider. */
    @Composable fun success() = AppSliderStyle(
        colors = SliderDefaults.colors(
            thumbColor       = Color(0xFF2E7D32),
            activeTrackColor = Color(0xFF2E7D32),
        ),
    )

    /** Warning / amber slider. */
    @Composable fun warning() = AppSliderStyle(
        colors = SliderDefaults.colors(
            thumbColor       = Color(0xFFF9A825),
            activeTrackColor = Color(0xFFF9A825),
        ),
    )

    /** Danger / error slider. */
    @Composable fun danger() = AppSliderStyle(
        colors = SliderDefaults.colors(
            thumbColor       = MaterialTheme.colorScheme.error,
            activeTrackColor = MaterialTheme.colorScheme.error,
        ),
    )

    /** Secondary slider — uses secondary color. */
    @Composable fun secondary() = AppSliderStyle(
        colors = SliderDefaults.colors(
            thumbColor       = MaterialTheme.colorScheme.secondary,
            activeTrackColor = MaterialTheme.colorScheme.secondary,
        ),
    )

    /** Tertiary / accent slider. */
    @Composable fun tertiary() = AppSliderStyle(
        colors = SliderDefaults.colors(
            thumbColor       = MaterialTheme.colorScheme.tertiary,
            activeTrackColor = MaterialTheme.colorScheme.tertiary,
        ),
    )
}
