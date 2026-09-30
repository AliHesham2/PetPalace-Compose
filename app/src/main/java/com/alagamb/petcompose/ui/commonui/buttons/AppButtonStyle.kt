package com.alagamb.petcompose.ui.commonui.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.ui.commonui.customize.AppShape


@Stable
data class AppButtonStyle(
    val colors: ButtonColors,
    val shape: Shape = AppShape.Pill,
    val border: BorderStroke? = null,
    val shadowElevation: Dp = 0.dp,
) {
    companion object {
        @Composable
        fun create(
            containerColor: Color = MaterialTheme.colorScheme.primary,
            contentColor: Color = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            shape: Shape = AppShape.Pill,
            borderColor: Color = Color.Transparent,
            borderWidth: Dp = 0.dp,
            shadowElevation: Dp = 0.dp,
        ) = AppButtonStyle(
            colors = ButtonDefaults.buttonColors(
                containerColor         = containerColor,
                contentColor           = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor   = disabledContentColor,
            ),
            shape           = shape,
            border          = if (borderWidth > 0.dp && borderColor != Color.Transparent)
                BorderStroke(borderWidth, borderColor) else null,
            shadowElevation = shadowElevation,
        )
    }
}

// ── Pre-built Button styles ────────────────────────────────────────────────

/** Returns the default M3 theme styles at call-site */
object AppButtonStyles {

    @Composable fun primary() = AppButtonStyle(
        colors = ButtonDefaults.buttonColors(),
        shape  = AppShape.Pill,
    )

    @Composable fun tonal() = AppButtonStyle(
        colors = ButtonDefaults.filledTonalButtonColors(),
        shape  = AppShape.Pill,
    )

    @Composable fun outlined() = AppButtonStyle(
        colors = ButtonDefaults.outlinedButtonColors(),
        shape  = AppShape.Pill,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )


    @Composable fun danger() = AppButtonStyle(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor   = MaterialTheme.colorScheme.onError,
        ),
        shape  = AppShape.Pill,
    )


    @Composable fun ghost() = AppButtonStyle(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor   = MaterialTheme.colorScheme.primary,
        ),
        shape  = AppShape.Pill,
    )


    @Composable fun squarePrimary() = AppButtonStyle(
        colors = ButtonDefaults.buttonColors(),
        shape  = AppShape.Medium,
    )

    @Composable fun neutral() = AppButtonStyle(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor   = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        shape = AppShape.Pill,
    )

    @Composable fun success() = AppButtonStyle(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor   = MaterialTheme.colorScheme.onTertiary,
        ),
        shape = AppShape.Pill,
    )
}
