package com.alagamb.petcompose.ui.commonui.menus

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

// ─────────────────────────────────────────────────────────────────────────────
// Data model for menu items
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Represents a single item in an [AppDropdownMenu].
 *
 * @param label         Text displayed for the item.
 * @param leadingIcon   Optional icon at the start.
 * @param trailingIcon  Optional icon at the end (shortcut hint, arrow, etc.).
 * @param enabled       When false the item is dimmed and ignores clicks.
 * @param isDivider     When true a [HorizontalDivider] is rendered instead of an item.
 * @param onClick       Action triggered when this item is tapped.
 */
data class AppMenuItem(
    val label: String = "",
    val leadingIcon: ImageVector? = null,
    val trailingIcon: ImageVector? = null,
    val enabled: Boolean = true,
    val isDivider: Boolean = false,
    val onClick: () -> Unit = {},
)

// ─────────────────────────────────────────────────────────────────────────────
// AppDropdownMenu  (contextual / overflow menu)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A contextual dropdown menu that opens relative to an anchor composable.
 *
 * Usage:
 * ```
 * AppDropdownMenu(
 *     anchor = {
 *         IconButton(onClick = { /* expanded managed internally */ }) {
 *             Icon(Icons.Default.MoreVert, null)
 *         }
 *     },
 *     items = listOf(
 *         AppMenuItem("Edit",   leadingIcon = Icons.Default.Edit)   { doEdit() },
 *         AppMenuItem("", isDivider = true),
 *         AppMenuItem("Delete", leadingIcon = Icons.Default.Delete) { doDelete() },
 *     )
 * )
 * ```
 *
 * @param anchor   The composable that triggers the menu (button, icon, etc.).
 * @param items    Ordered list of [AppMenuItem]s to display.
 * @param modifier Modifier applied to the outer [Box].
 * @param colors   Override text / icon / disabled colors for all items.
 */
@Composable
fun AppDropdownMenu(
    anchor: @Composable (openMenu: () -> Unit) -> Unit,
    items: List<AppMenuItem>,
    modifier: Modifier = Modifier,
    colors: MenuItemColors = MenuDefaults.itemColors(),
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        // Anchor — caller receives the openMenu lambda
        anchor { expanded = true }

        DropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                if (item.isDivider) {
                    HorizontalDivider()
                } else {
                    DropdownMenuItem(
                        text         = { Text(item.label) },
                        onClick      = {
                            expanded = false
                            item.onClick()
                        },
                        enabled      = item.enabled,
                        leadingIcon  = item.leadingIcon?.let { icon ->
                            { Icon(icon, contentDescription = null) }
                        },
                        trailingIcon = item.trailingIcon?.let { icon ->
                            { Icon(icon, contentDescription = null) }
                        },
                        colors       = colors,
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppRawDropdownMenu  (escape hatch — provide your own content)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Raw dropdown menu with full control over the item content.
 * Use when [AppDropdownMenu] doesn't fit (nested menus, custom rows, etc.).
 *
 * @param expanded         Whether the menu is visible.
 * @param onDismissRequest Called to close the menu.
 * @param content          Your raw [DropdownMenuItem] / [HorizontalDivider] composables.
 */
@Composable
fun AppRawDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    DropdownMenu(
        expanded         = expanded,
        onDismissRequest = onDismissRequest,
        modifier         = modifier,
    ) {
        content()
    }
}
