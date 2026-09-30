package com.alagamb.petcompose.ui.commonui.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.alagamb.petcompose.ui.commonui.customize.AppShape

// ─────────────────────────────────────────────────────────────────────────────
// AppCardStyle — reusable style for Card / ElevatedCard / OutlinedCard
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppCard(style = AppCardStyles.elevated()) { ... }
//   AppCard(style = AppCardStyles.hero())     { ... }
//   AppCard(style = AppCardStyle.create(
//       containerColor = Color(0xFF1A1A2E),
//       elevation      = 8.dp,
//   )) { ... }
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles all visual tokens for [AppCard].
 *
 * @param colors    Container / content / disabled colors.
 * @param shape     Corner shape — pass any [AppShape] token or a custom shape.
 * @param border    Optional [BorderStroke] — pass null for no border.
 * @param elevation Shadow depth in dp.
 */
@Stable
data class AppCardStyle(
    val colors: CardColors,
    val shape: Shape = AppShape.Medium,
    val border: BorderStroke? = null,
    val elevation: Dp = 0.dp,
) {
    companion object {
        /** Factory from raw colors. */
        @Composable
        fun create(
            containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape: Shape = AppShape.Medium,
            borderColor: Color = Color.Transparent,
            borderWidth: Dp = 0.dp,
            elevation: Dp = 0.dp,
        ) = AppCardStyle(
            colors = CardDefaults.cardColors(
                containerColor = containerColor,
                contentColor   = contentColor,
            ),
            shape     = shape,
            border    = if (borderWidth > 0.dp && borderColor != Color.Transparent)
                BorderStroke(borderWidth, borderColor) else null,
            elevation = elevation,
        )
    }
}

// ── Pre-built Card styles ──────────────────────────────────────────────────

object AppCardStyles {

    /** Standard filled card — [surfaceVariant] bg. */
    @Composable fun filled() = AppCardStyle(
        colors = CardDefaults.cardColors(),
        shape  = AppShape.Medium,
    )

    /** Elevated card — [surface] bg with shadow. */
    @Composable fun elevated() = AppCardStyle(
        colors    = CardDefaults.elevatedCardColors(),
        shape     = AppShape.Medium,
        elevation = 4.dp,
    )

    /** Outlined card — [surface] bg, [outline] 1 dp border. */
    @Composable fun outlined() = AppCardStyle(
        colors  = CardDefaults.outlinedCardColors(),
        shape   = AppShape.Medium,
        border  = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    )

    /** Hero card — large rounded, [primaryContainer] background. */
    @Composable fun hero() = AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor   = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        shape     = AppShape.XLarge,
        elevation = 6.dp,
    )

    /** Dark surface card — [surface] with tonal elevation overlay. */
    @Composable fun surface() = AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        shape     = AppShape.Large,
        elevation = 2.dp,
    )

    /** Secondary container card — good for info or feature highlight cards. */
    @Composable fun secondary() = AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor   = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        shape     = AppShape.Medium,
        elevation = 2.dp,
    )

    /** Tertiary container card — accent / highlight card. */
    @Composable fun tertiary() = AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor   = MaterialTheme.colorScheme.onTertiaryContainer,
        ),
        shape     = AppShape.Medium,
        elevation = 0.dp,
    )
}
