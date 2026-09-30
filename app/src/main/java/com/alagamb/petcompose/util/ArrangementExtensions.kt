package com.alagamb.petcompose.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/**
 * Creates an adaptive horizontal arrangement for rows and lazy rows.
 *
 * Behavior:
 * - If the items fit within the available width (`totalChildrenSize < totalSize`),
 *   they are centered horizontally.
 * - If the items exceed the available width (`totalChildrenSize >= totalSize`),
 *   they start at the beginning (0) so the user can scroll naturally without clipping.
 *
 * @param spacing The space between adjacent items.
 */
fun Arrangement.adaptiveCentered(spacing: Dp): Arrangement.Horizontal = object : Arrangement.Horizontal {
    override val spacing: Dp = spacing

    override fun Density.arrange(
        totalSize: Int,
        sizes: IntArray,
        layoutDirection: LayoutDirection,
        outPositions: IntArray
    ) {
        val spacingPx = spacing.roundToPx()
        val totalChildrenSize = sizes.sum() + spacingPx * (sizes.size - 1).coerceAtLeast(0)
        var currentPosition = if (totalChildrenSize < totalSize) {
            (totalSize - totalChildrenSize) / 2
        } else {
            0
        }
        for (i in sizes.indices) {
            outPositions[i] = currentPosition
            currentPosition += sizes[i] + spacingPx
        }
    }
}
