package com.alagamb.petcompose.ui.commonui.indicators

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppProgressStyle — reusable style for AppLinearProgress / AppCircularProgress
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppLinearProgress(progress = 0.6f,
//       color      = AppProgressStyles.success().color,
//       trackColor = AppProgressStyles.success().trackColor)
//
//   AppCircularProgress(
//       color       = AppProgressStyles.danger().color,
//       strokeWidth = AppProgressStyles.danger().strokeWidth)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppLinearProgress], [AppCircularProgress],
 * and [AppLoadingOverlay].
 *
 * @param color       The filled / active progress color.
 * @param trackColor  The background / inactive track color.
 * @param strokeWidth Thickness of the arc (circular) or bar (linear).
 * @param strokeCap   End-cap style — [StrokeCap.Round] looks modern and smooth.
 * @param gapSize     Gap between fill and track ends (linear only).
 */
@Stable
data class AppProgressStyle(
    val color: Color,
    val trackColor: Color,
    val strokeWidth: Dp = 4.dp,
    val strokeCap: StrokeCap = StrokeCap.Round,
    val gapSize: Dp = 4.dp,
) {
    companion object {
        /**
         * Factory — create a fully custom progress style.
         * Only specify what you want to override.
         */
        @Composable
        fun create(
            color: Color = MaterialTheme.colorScheme.primary,
            trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
            strokeWidth: Dp = 4.dp,
            strokeCap: StrokeCap = StrokeCap.Round,
            gapSize: Dp = 4.dp,
        ) = AppProgressStyle(
            color       = color,
            trackColor  = trackColor,
            strokeWidth = strokeWidth,
            strokeCap   = strokeCap,
            gapSize     = gapSize,
        )
    }
}

// ── Pre-built Progress styles ──────────────────────────────────────────────

object AppProgressStyles {

    /** Default M3 indicator — follows theme [primary]. */
    @Composable fun default() = AppProgressStyle.create()

    /** Success / green indicator. */
    @Composable fun success() = AppProgressStyle.create(
        color      = Color(0xFF2E7D32),
        trackColor = Color(0xFFE8F5E9),
    )

    /** Warning / amber indicator. */
    @Composable fun warning() = AppProgressStyle.create(
        color      = Color(0xFFF9A825),
        trackColor = Color(0xFFFFF8E1),
    )

    /** Danger / error indicator. */
    @Composable fun danger() = AppProgressStyle(
        color      = MaterialTheme.colorScheme.error,
        trackColor = MaterialTheme.colorScheme.errorContainer,
    )

    /** Secondary / tonal indicator. */
    @Composable fun secondary() = AppProgressStyle(
        color      = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.secondaryContainer,
    )

    /**
     * Thin indicator — 2 dp stroke for subtle loading hints.
     * Works well for pull-to-refresh or top-of-screen loaders.
     */
    @Composable fun thin() = AppProgressStyle.create(
        color       = MaterialTheme.colorScheme.primary,
        strokeWidth = 2.dp,
        strokeCap   = StrokeCap.Butt,
        gapSize     = 2.dp,
    )

    /**
     * Thick indicator — 8 dp stroke for large prominent loaders.
     * Use for full-screen loading states.
     */
    @Composable fun thick() = AppProgressStyle.create(
        color       = MaterialTheme.colorScheme.primary,
        strokeWidth = 8.dp,
        strokeCap   = StrokeCap.Round,
        gapSize     = 4.dp,
    )

    /**
     * Gradient-like tertiary indicator — uses tertiary color for a contrast pop.
     */
    @Composable fun tertiary() = AppProgressStyle(
        color      = MaterialTheme.colorScheme.tertiary,
        trackColor = MaterialTheme.colorScheme.tertiaryContainer,
    )
}
