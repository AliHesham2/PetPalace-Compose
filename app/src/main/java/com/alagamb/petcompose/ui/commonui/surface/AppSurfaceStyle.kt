package com.alagamb.petcompose.ui.commonui.surface

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.ui.commonui.customize.AppShape

// ─────────────────────────────────────────────────────────────────────────────
// AppSurfaceStyle — Style token for AppSurface
//
// FOLLOWS SAME PATTERN as AppCardStyle, AppButtonStyle, AppBottomBarStyle.
// Encapsulates background, content color, shape, elevations, and borders.
// ─────────────────────────────────────────────────────────────────────────────

@Immutable
data class AppSurfaceStyle(
    val color          : Color,
    val contentColor   : Color,
    val shape          : Shape = AppShape.Medium,
    val tonalElevation : Dp    = 0.dp,
    val shadowElevation: Dp    = 0.dp,
    val border         : BorderStroke? = null,
) {
    companion object {
        /**
         * Factory from raw styling parameters with MaterialTheme fallbacks.
         */
        @Composable
        fun create(
            color          : Color         = MaterialTheme.colorScheme.surface,
            contentColor   : Color         = MaterialTheme.colorScheme.onSurface,
            shape          : Shape         = AppShape.Medium,
            tonalElevation : Dp            = 0.dp,
            shadowElevation: Dp            = 0.dp,
            border         : BorderStroke? = null,
        ) = AppSurfaceStyle(
            color           = color,
            contentColor    = contentColor,
            shape           = shape,
            tonalElevation  = tonalElevation,
            shadowElevation = shadowElevation,
            border          = border,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppSurfaceStyles — Pre-built factory presets
// ─────────────────────────────────────────────────────────────────────────────
object AppSurfaceStyles {

    /** Default M3 Surface — theme surface and onSurface */
    @Composable
    fun default() = AppSurfaceStyle.create()

    /** Pill / Stadium shape surface — perfect for tags, chips, badges */
    @Composable
    fun pill(
        color: Color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
    ) = AppSurfaceStyle.create(
        shape = AppShape.Pill,
        color = color,
        contentColor = contentColor
    )

    /** Elevated surface with shadow */
    @Composable
    fun elevated(
        elevation: Dp = 3.dp,
        shape: Shape = AppShape.Medium
    ) = AppSurfaceStyle.create(
        shape = shape,
        shadowElevation = elevation
    )

    /** Outlined surface with a border stroke */
    @Composable
    fun outlined(
        shape: Shape = AppShape.Medium,
        borderColor: Color = MaterialTheme.colorScheme.outlineVariant
    ) = AppSurfaceStyle.create(
        shape = shape,
        border = BorderStroke(1.dp, borderColor)
    )

    /** Primary container tinted surface */
    @Composable
    fun primaryContainer(
        shape: Shape = AppShape.Medium
    ) = AppSurfaceStyle.create(
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shape = shape
    )

    /** Translucent dark overlay — great for floating tags on images */
    @Composable
    fun translucent(
        shape: Shape = AppShape.Pill,
        alpha: Float = 0.35f
    ) = AppSurfaceStyle.create(
        color = Color.Black.copy(alpha = alpha),
        contentColor = Color.White,
        shape = shape
    )

    /** White frosted floating surface with shadow */
    @Composable
    fun floatingWhite(
        shape: Shape = AppShape.Pill,
        alpha: Float = 0.9f
    ) = AppSurfaceStyle.create(
        color = Color.White.copy(alpha = alpha),
        contentColor = Color(0xFF1E1E2E),
        shape = shape,
        shadowElevation = 2.dp
    )
}
