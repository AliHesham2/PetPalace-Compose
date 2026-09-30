package com.alagamb.petcompose.ui.commonui.tooltips

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.RichTooltipColors
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppPlainTooltip  (short text bubble — long-press or programmatic)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Wraps [anchor] in a [TooltipBox] showing a plain text tooltip.
 *
 * The tooltip is shown on **long-press** by default (enableUserInput = true).
 * To show it programmatically, pass a [TooltipState] and call `state.show()`.
 *
 * Usage:
 * ```
 * AppPlainTooltip(tooltipText = "Delete this item") {
 *     IconButton(onClick = { }) {
 *         Icon(Icons.Default.Delete, contentDescription = "Delete")
 *     }
 * }
 * ```
 *
 * @param tooltipText      The text to display inside the tooltip bubble.
 * @param modifier         Modifier applied to the [TooltipBox].
 * @param state            External [TooltipState] — useful for programmatic control.
 * @param enableUserInput  When true, long-pressing [anchor] shows the tooltip.
 * @param containerColor   Tooltip background color — defaults to [inverseSurface].
 * @param contentColor     Text color inside the tooltip — defaults to [inverseOnSurface].
 * @param shape            Tooltip corner shape.
 * @param shadowElevation  Shadow under the tooltip bubble.
 * @param anchor           The composable that the tooltip is attached to.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPlainTooltip(
    tooltipText: String,
    modifier: Modifier = Modifier,
    state: TooltipState = rememberTooltipState(),
    enableUserInput: Boolean = true,
    containerColor: Color = TooltipDefaults.plainTooltipContainerColor,
    contentColor: Color = TooltipDefaults.plainTooltipContentColor,
    shape: Shape = TooltipDefaults.plainTooltipContainerShape,
    shadowElevation: Dp = 0.dp,
    anchor: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider  = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        state             = state,
        modifier          = modifier,
        enableUserInput   = enableUserInput,
        tooltip           = {
            PlainTooltip(
                containerColor  = containerColor,
                contentColor    = contentColor,
                shape           = shape,
                shadowElevation = shadowElevation,
            ) {
                Text(tooltipText)
            }
        },
    ) {
        anchor()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppRichTooltip  (title + body + optional action link)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Wraps [anchor] in a [TooltipBox] showing a rich tooltip with an optional
 * [title], body [text], and an [action] button.
 *
 * Usage:
 * ```
 * AppRichTooltip(
 *     title  = "Pro Tip",
 *     text   = "Long-press any item to access quick actions.",
 *     action = { TextButton(onClick = { }) { Text("Got it") } }
 * ) {
 *     IconButton(onClick = { }) {
 *         Icon(Icons.Default.Info, contentDescription = "Info")
 *     }
 * }
 * ```
 *
 * @param text      Body text of the tooltip.
 * @param title     Optional title composable shown at the top in bold.
 * @param action    Optional action composable shown at the bottom (e.g. a [TextButton]).
 * @param colors    Override container, content, title, and action colors.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRichTooltip(
    text: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    action: @Composable (() -> Unit)? = null,
    state: TooltipState = rememberTooltipState(isPersistent = true),
    enableUserInput: Boolean = true,
    colors: RichTooltipColors = TooltipDefaults.richTooltipColors(),
    anchor: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
        state            = state,
        modifier         = modifier,
        enableUserInput  = enableUserInput,
        tooltip          = {
            RichTooltip(
                title  = title?.let { { Text(it) } },
                action = action,
                colors = colors,
                text   = { Text(text) },
            )
        },
    ) {
        anchor()
    }
}
