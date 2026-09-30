package com.alagamb.petcompose.ui.commonui.customize

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppModifier — reusable Modifier extension functions
// (replaces XML drawables used on raw View backgrounds)
// ─────────────────────────────────────────────────────────────────────────────
//
// ⚠️  Use these on layout containers: Box, Column, Row, Surface, LazyColumn items.
// ⚠️  Do NOT expect them to override internal backgrounds of M3 components
//     like Button, Card, Chip — those are controlled by their `colors` parameter.
//
// Analogy to XML:
//   <shape><solid color="…"/><corners radius="…"/></shape>  → Modifier.appBackground(...)
//   <shape><stroke …/></shape>                               → Modifier.appBorder(...)
//   <shape><gradient …/></shape>                             → Modifier.appGradientBackground(...)
// ─────────────────────────────────────────────────────────────────────────────

// ── Solid background + clip ────────────────────────────────────────────────

/**
 * Solid color background with clipped corners.
 * Equivalent to `<solid color="…"/>` + `<corners radius="…"/>` in XML.
 *
 * Usage:
 * ```
 * Box(modifier = Modifier.appBackground(Color(0xFF6200EA), AppShape.Medium))
 * ```
 */
fun Modifier.appBackground(
    color: Color,
    shape: Shape = AppShape.None,
): Modifier = this
    .clip(shape)
    .background(color = color, shape = shape)

// ── Gradient background + clip ─────────────────────────────────────────────

/**
 * Horizontal gradient background with clipped corners.
 * Equivalent to `<gradient android:type="linear" …/>` in XML.
 *
 * Usage:
 * ```
 * Box(
 *   modifier = Modifier.appGradientBackground(
 *     colors = listOf(Color(0xFF6200EA), Color(0xFF03DAC5)),
 *     shape  = AppShape.Large
 *   )
 * )
 * ```
 */
fun Modifier.appGradientBackground(
    colors: List<Color>,
    shape: Shape = AppShape.None,
    isVertical: Boolean = false,
): Modifier {
    val brush = if (isVertical) {
        Brush.verticalGradient(colors)
    } else {
        Brush.horizontalGradient(colors)
    }
    return this
        .clip(shape)
        .background(brush = brush, shape = shape)
}

/**
 * Radial gradient background with clipped corners.
 */
fun Modifier.appRadialBackground(
    colors: List<Color>,
    shape: Shape = AppShape.None,
): Modifier = this
    .clip(shape)
    .background(brush = Brush.radialGradient(colors), shape = shape)

// ── Border / Stroke ────────────────────────────────────────────────────────

/**
 * Adds a solid-color border around the composable.
 * Equivalent to `<stroke android:width="…" android:color="…"/>` in XML.
 *
 * Usage:
 * ```
 * Box(modifier = Modifier.appBorder(Color(0xFF6200EA), AppShape.Medium, 2.dp))
 * ```
 */
fun Modifier.appBorder(
    color: Color,
    shape: Shape = AppShape.None,
    width: Dp = 1.dp,
): Modifier = this.border(
    border = BorderStroke(width, color),
    shape  = shape,
)

/**
 * Gradient-colored border.
 */
fun Modifier.appGradientBorder(
    colors: List<Color>,
    shape: Shape = AppShape.None,
    width: Dp = 1.dp,
): Modifier = this.border(
    border = BorderStroke(width, Brush.linearGradient(colors)),
    shape  = shape,
)

// ── Background + Border combined (like a solid drawable with stroke) ────────

/**
 * Solid background + border in one call.
 * Equivalent to `<solid/>` + `<stroke/>` + `<corners/>` in XML.
 *
 * Usage:
 * ```
 * Box(
 *   modifier = Modifier.appFill(
 *     fillColor   = Color.White,
 *     borderColor = Color(0xFF6200EA),
 *     shape       = AppShape.Medium
 *   )
 * )
 * ```
 */
fun Modifier.appFill(
    fillColor: Color,
    borderColor: Color = Color.Transparent,
    shape: Shape = AppShape.None,
    borderWidth: Dp = 1.dp,
): Modifier = this
    .clip(shape)
    .background(color = fillColor, shape = shape)
    .border(BorderStroke(borderWidth, borderColor), shape)

// ── Shadow / Elevation ─────────────────────────────────────────────────────

/**
 * Draws a shadow below the composable, respecting the given [shape].
 * Use on raw composables — for M3 components use their `elevation` parameter.
 *
 * Usage:
 * ```
 * Box(modifier = Modifier.appShadow(8.dp, AppShape.Large))
 * ```
 */
fun Modifier.appShadow(
    elevation: Dp,
    shape: Shape = AppShape.None,
    ambientColor: Color = Color.Black.copy(alpha = 0.1f),
    spotColor: Color = Color.Black.copy(alpha = 0.25f),
): Modifier = this.shadow(
    elevation       = elevation,
    shape           = shape,
    ambientColor    = ambientColor,
    spotColor       = spotColor,
)

// ── Full card-style surface (background + shadow + clip) ───────────────────

/**
 * All-in-one: shadow + solid fill + optional border.
 * The Compose equivalent of a card-shaped XML drawable.
 *
 * Usage:
 * ```
 * Column(
 *   modifier = Modifier
 *     .fillMaxWidth()
 *     .appSurface(
 *       fillColor   = Color.White,
 *       shape       = AppShape.Large,
 *       shadowDp    = 8.dp
 *     )
 *     .padding(16.dp)
 * )
 * ```
 */
fun Modifier.appSurface(
    fillColor: Color,
    shape: Shape = AppShape.None,
    shadowDp: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
): Modifier = this
    .shadow(elevation = shadowDp, shape = shape)
    .clip(shape)
    .background(color = fillColor, shape = shape)
    .then(
        if (borderWidth > 0.dp && borderColor != Color.Transparent)
            Modifier.border(BorderStroke(borderWidth, borderColor), shape)
        else Modifier
    )
