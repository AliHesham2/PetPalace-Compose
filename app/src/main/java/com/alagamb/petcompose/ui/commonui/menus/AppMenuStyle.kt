package com.alagamb.petcompose.ui.commonui.menus

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.alagamb.petcompose.ui.commonui.customize.AppShape

// ─────────────────────────────────────────────────────────────────────────────
// AppMenuStyle — reusable style for AppDropdownMenu / AppRawDropdownMenu
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppDropdownMenu(
//       anchor = { openMenu -> IconButton(onClick = openMenu) { ... } },
//       items  = menuItems,
//       style  = AppMenuStyles.default(),
//   )
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppDropdownMenu] and [AppRawDropdownMenu].
 *
 * @param itemColors      Text / icon / disabled colors for each [DropdownMenuItem].
 * @param containerColor  Background color of the menu surface (the popup container).
 * @param shape           Corner shape of the menu popup.
 */
@Stable
data class AppMenuStyle(
    val itemColors: MenuItemColors,
    val containerColor: Color,
    val shape: Shape = AppShape.Small,
) {
    companion object {
        /**
         * Factory — create a fully custom menu style.
         * Only specify what you want to override.
         */
        @Composable
        fun create(
            textColor: Color = MaterialTheme.colorScheme.onSurface,
            leadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            trailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledLeadingIconColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledTrailingIconColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            containerColor: Color = MaterialTheme.colorScheme.surface,
            shape: Shape = AppShape.Small,
        ) = AppMenuStyle(
            itemColors = MenuDefaults.itemColors(
                textColor              = textColor,
                leadingIconColor       = leadingIconColor,
                trailingIconColor      = trailingIconColor,
                disabledTextColor      = disabledTextColor,
                disabledLeadingIconColor  = disabledLeadingIconColor,
                disabledTrailingIconColor = disabledTrailingIconColor,
            ),
            containerColor = containerColor,
            shape          = shape,
        )
    }
}

// ── Pre-built Menu styles ──────────────────────────────────────────────────

object AppMenuStyles {

    /** Default M3 dropdown menu — [surface] bg, default icon/text colors. */
    @Composable fun default() = AppMenuStyle.create()

    /**
     * Tinted menu — [surfaceVariant] bg for a subtle container distinction.
     * Use when the menu sits on top of a light surface.
     */
    @Composable fun tinted() = AppMenuStyle.create(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )

    /**
     * Primary tinted menu — [primaryContainer] bg.
     * Use for branded / featured menus.
     */
    @Composable fun primary() = AppMenuStyle.create(
        containerColor    = MaterialTheme.colorScheme.primaryContainer,
        textColor         = MaterialTheme.colorScheme.onPrimaryContainer,
        leadingIconColor  = MaterialTheme.colorScheme.onPrimaryContainer,
        trailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    /**
     * Danger menu — text and icons tinted [error].
     * Use for destructive-only action menus (delete, remove, etc.).
     */
    @Composable fun danger() = AppMenuStyle.create(
        textColor        = MaterialTheme.colorScheme.error,
        leadingIconColor = MaterialTheme.colorScheme.error,
    )
}
