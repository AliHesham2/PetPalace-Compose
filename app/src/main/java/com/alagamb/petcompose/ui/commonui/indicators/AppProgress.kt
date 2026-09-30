package com.alagamb.petcompose.ui.commonui.indicators

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppLinearProgress  (horizontal bar — determinate or indeterminate)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Horizontal linear progress bar.
 *
 * Pass [progress] = null for an **indeterminate** (infinite animation) bar.
 * Pass [progress] in 0f..1f for a **determinate** bar.
 *
 * @param progress   0f..1f for determinate; null for indeterminate.
 * @param modifier   Layout modifier — use [Modifier.fillMaxWidth()] to stretch.
 * @param color      The fill (progress) color — defaults to [primary].
 * @param trackColor The background track color — defaults to [surfaceVariant].
 * @param strokeCap  End cap style: [StrokeCap.Round] (default), [StrokeCap.Butt], [StrokeCap.Square].
 * @param gapSize    Gap between the progress fill and the track ends.
 */
@Composable
fun AppLinearProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeCap: StrokeCap = StrokeCap.Butt,
    gapSize: Dp = 4.dp,
) {
    if (progress != null) {
        LinearProgressIndicator(
            progress   = { progress.coerceIn(0f, 1f) },
            modifier   = modifier,
            color      = color,
            trackColor = trackColor,
            strokeCap  = strokeCap,
            gapSize    = gapSize,
        )
    } else {
        LinearProgressIndicator(
            modifier   = modifier,
            color      = color,
            trackColor = trackColor,
            strokeCap  = strokeCap,
            gapSize    = gapSize,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppCircularProgress  (spinning circle — determinate or indeterminate)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Circular progress indicator.
 *
 * Pass [progress] = null for an **indeterminate** (spinning) indicator.
 * Pass [progress] in 0f..1f for a **determinate** arc.
 *
 * @param progress    0f..1f for determinate; null for indeterminate.
 * @param modifier    Size modifier — e.g. [Modifier.size(48.dp)].
 * @param color       Arc fill color — defaults to [primary].
 * @param trackColor  Background circle color — defaults to [surfaceVariant].
 * @param strokeWidth Thickness of the arc — default is 4 dp.
 * @param strokeCap   End cap style — [StrokeCap.Round] gives nicer rounded ends.
 */
@Composable
fun AppCircularProgress(
    progress: Float? = null,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    strokeWidth: Dp = 4.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    if (progress != null) {
        CircularProgressIndicator(
            progress    = { progress.coerceIn(0f, 1f) },
            modifier    = modifier,
            color       = color,
            trackColor  = trackColor,
            strokeWidth = strokeWidth,
            strokeCap   = strokeCap,
        )
    } else {
        CircularProgressIndicator(
            modifier    = modifier,
            color       = color,
            trackColor  = trackColor,
            strokeWidth = strokeWidth,
            strokeCap   = strokeCap,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppLoadingOverlay  (full-area centered spinner — for loading states)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Centers a [AppCircularProgress] inside a [Box] that fills the available space.
 * Drop this into any screen that needs a full-area loading state.
 *
 * Usage:
 * ```
 * if (isLoading) AppLoadingOverlay()
 * ```
 */
@Composable
fun AppLoadingOverlay(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Box(
        modifier         = modifier,
        contentAlignment = Alignment.Center,
    ) {
        AppCircularProgress(
            modifier = Modifier.size(size),
            color    = color,
        )
    }
}
