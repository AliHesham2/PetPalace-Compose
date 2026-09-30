package com.alagamb.petcompose.ui.commonui.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape

// AppCardStyle is in the same package (cards) — no import needed

// ─────────────────────────────────────────────────────────────────────────────
// AppCard  (Filled — subtle tonal background, no border)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Filled card.
 *
 * You can style it two ways — pick one:
 *
 * **1. Style object (recommended for reuse)**
 * ```
 * AppCard(style = AppCardStyles.hero()) { ... }
 * AppCard(style = AppCardStyles.outlined()) { ... }
 * // Custom:
 * val myStyle = AppCardStyle.create(containerColor = Color(0xFF1E1E1E), shape = AppShape.XLarge)
 * AppCard(style = myStyle) { ... }
 * ```
 *
 * **2. Individual parameters (quick overrides)**
 * ```
 * AppCard(shape = AppShape.Large, colors = CardDefaults.cardColors(...)) { ... }
 * ```
 *
 * @param modifier  Layout modifier (size, padding, etc.).
 * @param style     Optional [AppCardStyle] — overrides [shape], [colors], [border], [elevation].
 * @param shape     Corner shape — ignored when [style] is provided.
 * @param colors    Colors — ignored when [style] is provided.
 * @param elevation Shadow depth — ignored when [style] is provided.
 * @param border    Border — ignored when [style] is provided.
 * @param onClick   When provided the card becomes clickable with a ripple.
 * @param content   Composable content inside the card (Column scope).
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    style: AppCardStyle? = null,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    // Style object wins over individual params when provided
    val resolvedShape     = style?.shape ?: shape
    val resolvedColors    = style?.colors ?: colors
    val resolvedBorder    = style?.border ?: border
    val resolvedElevation = style?.elevation?.let {
        CardDefaults.cardElevation(defaultElevation = it)
    } ?: elevation

    if (onClick != null) {
        Card(
            onClick   = onClick,
            modifier  = modifier,
            shape     = resolvedShape,
            colors    = resolvedColors,
            elevation = resolvedElevation,
            border    = resolvedBorder,
            content   = content,
        )
    } else {
        Card(
            modifier  = modifier,
            shape     = resolvedShape,
            colors    = resolvedColors,
            elevation = resolvedElevation,
            border    = resolvedBorder,
            content   = content,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppElevatedCard  (shadow, [surface] background)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Elevated card — [surface] background lifted by shadow.
 * Good for content that needs to stand out from the page background.
 */
@Composable
fun AppElevatedCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.elevatedShape,
    colors: CardColors = CardDefaults.elevatedCardColors(),
    elevation: CardElevation = CardDefaults.elevatedCardElevation(),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        ElevatedCard(
            onClick   = onClick,
            modifier  = modifier,
            shape     = shape,
            colors    = colors,
            elevation = elevation,
            content   = content,
        )
    } else {
        ElevatedCard(
            modifier  = modifier,
            shape     = shape,
            colors    = colors,
            elevation = elevation,
            content   = content,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppOutlinedCard  (border, no shadow, [surface] background)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Outlined card — [surface] background with [outline] colored border.
 * Use for content that should be clearly bounded but not elevated.
 *
 * @param border Defaults to a 1 dp [outline] stroke.
 *               Pass a custom [BorderStroke] to change color/width.
 */
@Composable
fun AppOutlinedCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.outlinedShape,
    colors: CardColors = CardDefaults.outlinedCardColors(),
    elevation: CardElevation = CardDefaults.outlinedCardElevation(),
    border: BorderStroke = CardDefaults.outlinedCardBorder(),
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    if (onClick != null) {
        OutlinedCard(
            onClick   = onClick,
            modifier  = modifier,
            shape     = shape,
            colors    = colors,
            elevation = elevation,
            border    = border,
            content   = content,
        )
    } else {
        OutlinedCard(
            modifier  = modifier,
            shape     = shape,
            colors    = colors,
            elevation = elevation,
            border    = border,
            content   = content,
        )
    }
}
