package com.alagamb.petcompose.ui.commonui.buttons

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector

// ─────────────────────────────────────────────────────────────────────────────
// AppFab  (Standard 56×56 dp)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Standard Floating Action Button (56×56 dp).
 *
 * @param icon               The icon to display.
 * @param contentDescription Accessibility label for the icon.
 * @param onClick            Click callback.
 * @param modifier           Optional layout modifier.
 * @param shape              Corner shape — default is large rounded square (M3).
 * @param containerColor     Background color — default is [primaryContainer].
 * @param contentColor       Icon tint — defaults to the color that contrasts [containerColor].
 * @param elevation          Shadow levels per interaction state.
 */
@Composable
fun AppFab(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.shape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
) {
    FloatingActionButton(
        onClick        = onClick,
        modifier       = modifier,
        shape          = shape,
        containerColor = containerColor,
        contentColor   = contentColor,
        elevation      = elevation,
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppSmallFab  (40×40 dp — secondary / minor action)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Small FAB (40×40 dp). Use for secondary or minor actions.
 */
@Composable
fun AppSmallFab(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.smallShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
) {
    SmallFloatingActionButton(
        onClick        = onClick,
        modifier       = modifier,
        shape          = shape,
        containerColor = containerColor,
        contentColor   = contentColor,
        elevation      = elevation,
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppLargeFab  (96×96 dp — hero / primary screen action)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Large FAB (96×96 dp). Use for the single most important action on a screen.
 */
@Composable
fun AppLargeFab(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.largeShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
) {
    LargeFloatingActionButton(
        onClick        = onClick,
        modifier       = modifier,
        shape          = shape,
        containerColor = containerColor,
        contentColor   = contentColor,
        elevation      = elevation,
    ) {
        Icon(icon, contentDescription = contentDescription)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppExtendedFab  (icon + label, collapsible)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Extended FAB with an [icon] and [text] label.
 *
 * @param expanded When **true** the label is visible; **false** shows only the icon.
 *                 Animate this with scroll state to collapse on scroll-down.
 */
@Composable
fun AppExtendedFab(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    shape: Shape = FloatingActionButtonDefaults.extendedFabShape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
) {
    ExtendedFloatingActionButton(
        onClick        = onClick,
        modifier       = modifier,
        expanded       = expanded,
        shape          = shape,
        containerColor = containerColor,
        contentColor   = contentColor,
        elevation      = elevation,
        icon           = { Icon(icon, contentDescription = null) },
        text           = { Text(text) },
    )
}
