package com.alagamb.petcompose.ui.commonui.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppSurface — Reusable M3 Surface component
//
// WHAT IT DOES:
//   Wraps Material 3 Surface with full AppSurfaceStyle token support.
//   Supports both static surfaces and clickable surfaces with ripple.
//
// HOW TO USE:
//   AppSurface(style = AppSurfaceStyles.pill()) { ... }
//   AppSurface(style = AppSurfaceStyles.elevated()) { ... }
//   AppSurface(style = AppSurfaceStyle.create(color = Color.White)) { ... }
//   AppSurface(onClick = { ... }) { ... }
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun AppSurface(
    modifier       : Modifier         = Modifier,
    style          : AppSurfaceStyle? = null,
    shape          : Shape            = MaterialTheme.shapes.medium,
    color          : Color            = MaterialTheme.colorScheme.surface,
    contentColor   : Color            = MaterialTheme.colorScheme.onSurface,
    tonalElevation : Dp               = 0.dp,
    shadowElevation: Dp               = 0.dp,
    border         : BorderStroke?    = null,
    onClick        : (() -> Unit)?    = null,
    content        : @Composable () -> Unit,
) {
    val resolvedShape           = style?.shape ?: shape
    val resolvedColor           = style?.color ?: color
    val resolvedContentColor    = style?.contentColor ?: contentColor
    val resolvedTonalElevation  = style?.tonalElevation ?: tonalElevation
    val resolvedShadowElevation = style?.shadowElevation ?: shadowElevation
    val resolvedBorder          = style?.border ?: border

    if (onClick != null) {
        Surface(
            onClick         = onClick,
            modifier        = modifier,
            shape           = resolvedShape,
            color           = resolvedColor,
            contentColor    = resolvedContentColor,
            tonalElevation  = resolvedTonalElevation,
            shadowElevation = resolvedShadowElevation,
            border          = resolvedBorder,
            content         = content,
        )
    } else {
        Surface(
            modifier        = modifier,
            shape           = resolvedShape,
            color           = resolvedColor,
            contentColor    = resolvedContentColor,
            tonalElevation  = resolvedTonalElevation,
            shadowElevation = resolvedShadowElevation,
            border          = resolvedBorder,
            content         = content,
        )
    }
}
