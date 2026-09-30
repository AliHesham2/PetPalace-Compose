package com.alagamb.petcompose.ui.commonui.buttons

import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// ─────────────────────────────────────────────────────────────────────────────
// AppIconButton  (transparent background, ripple only)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Standard icon button — transparent background, only ripple on press.
 *
 * @param icon               The icon to display.
 * @param contentDescription Accessibility label.
 * @param onClick            Click callback.
 * @param modifier           Optional layout modifier.
 * @param enabled            When false, the button is dimmed and non-interactive.
 * @param tint               Tint color applied to the icon. Defaults to current content color.
 * @param colors             Override icon / container colors and disabled states.
 */
@Composable
fun AppIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
) {
    IconButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        colors   = colors,
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = contentDescription,
            tint               = tint,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppFilledIconButton  (solid primary fill)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Filled icon button — solid [primary] background.
 */
@Composable
fun AppFilledIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current,
    colors: IconButtonColors = IconButtonDefaults.filledIconButtonColors(),
) {
    FilledIconButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        colors   = colors,
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = contentDescription,
            tint               = tint,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppTonalIconButton  (secondary tonal fill)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Tonal icon button — [secondaryContainer] background, softer than filled.
 */
@Composable
fun AppTonalIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current,
    colors: IconButtonColors = IconButtonDefaults.filledTonalIconButtonColors(),
) {
    FilledTonalIconButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        colors   = colors,
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = contentDescription,
            tint               = tint,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppOutlinedIconButton  (no fill, outlined border)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Outlined icon button — transparent background with [outline] colored border.
 */
@Composable
fun AppOutlinedIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current,
    colors: IconButtonColors = IconButtonDefaults.outlinedIconButtonColors(),
) {
    OutlinedIconButton(
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        colors   = colors,
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = contentDescription,
            tint               = tint,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppIconToggleButton  (stateful — bookmark, star, etc.)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Toggle icon button. Pass two icons: one for checked state, one for unchecked.
 *
 * @param checked          Current toggle state.
 * @param onCheckedChange  State change callback.
 * @param checkedIcon      Icon shown when [checked] is true.
 * @param uncheckedIcon    Icon shown when [checked] is false.
 */
@Composable
fun AppIconToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    checkedIcon: ImageVector,
    uncheckedIcon: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = LocalContentColor.current,
    colors: IconToggleButtonColors = IconButtonDefaults.iconToggleButtonColors(),
) {
    IconToggleButton(
        checked         = checked,
        onCheckedChange = onCheckedChange,
        modifier        = modifier,
        enabled         = enabled,
        colors          = colors,
    ) {
        Icon(
            imageVector        = if (checked) checkedIcon else uncheckedIcon,
            contentDescription = contentDescription,
            tint               = tint,
        )
    }
}
