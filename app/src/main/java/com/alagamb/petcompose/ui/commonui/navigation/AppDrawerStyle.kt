package com.alagamb.petcompose.ui.commonui.navigation

import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItemColors
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.alagamb.petcompose.ui.commonui.customize.AppColors

// ─────────────────────────────────────────────────────────────────────────────
// AppDrawerStyle — Style token for AppModalDrawer / AppDrawerSheet
//
// FOLLOWS SAME PATTERN as AppBottomBarStyle & AppTopBarStyle:
//   One single immutable data class to encapsulate all visual styling
//   (colors, shapes, elevation, item highlights).
//
// FIELDS:
//   containerColor          → background color of the drawer sheet
//   selectedContainerColor  → background color of active drawer item
//   unselectedContainerColor→ background color of inactive drawer item
//   selectedIconColor       → icon color when item is active
//   unselectedIconColor     → icon color when item is inactive
//   selectedTextColor       → label text color when active
//   unselectedTextColor     → label text color when inactive
//   shape                   → outline shape of the drawer sheet
//   itemShape               → corner shape of each drawer item
//   tonalElevation          → M3 elevation for tonal surface color
// ─────────────────────────────────────────────────────────────────────────────

@Immutable
data class AppDrawerStyle(
    val containerColor          : Color,
    val selectedContainerColor  : Color,
    val unselectedContainerColor: Color,
    val selectedIconColor       : Color,
    val unselectedIconColor     : Color,
    val selectedTextColor       : Color,
    val unselectedTextColor     : Color,
    val shape                   : Shape,
    val itemShape               : Shape,
    val tonalElevation          : Dp,
) {
    /**
     * Converts to M3 NavigationDrawerItemColors required by NavigationDrawerItem.
     */
    @Composable
    fun toItemColors(): NavigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
        selectedContainerColor   = selectedContainerColor,
        unselectedContainerColor = unselectedContainerColor,
        selectedIconColor        = selectedIconColor,
        unselectedIconColor      = unselectedIconColor,
        selectedTextColor        = selectedTextColor,
        unselectedTextColor      = unselectedTextColor,
    )

    companion object {
        /**
         * Fully customizable factory with sensible MaterialTheme defaults.
         */
        @Composable
        fun create(
            containerColor          : Color = MaterialTheme.colorScheme.surface,
            selectedContainerColor  : Color = MaterialTheme.colorScheme.secondaryContainer,
            unselectedContainerColor: Color = Color.Transparent,
            selectedIconColor       : Color = MaterialTheme.colorScheme.onSecondaryContainer,
            unselectedIconColor     : Color = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedTextColor       : Color = MaterialTheme.colorScheme.onSecondaryContainer,
            unselectedTextColor     : Color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape                   : Shape = DrawerDefaults.shape,
            itemShape               : Shape = androidx.compose.foundation.shape.CircleShape,
            tonalElevation          : Dp    = DrawerDefaults.ModalDrawerElevation,
        ) = AppDrawerStyle(
            containerColor           = containerColor,
            selectedContainerColor   = selectedContainerColor,
            unselectedContainerColor = unselectedContainerColor,
            selectedIconColor        = selectedIconColor,
            unselectedIconColor      = unselectedIconColor,
            selectedTextColor        = selectedTextColor,
            unselectedTextColor      = unselectedTextColor,
            shape                    = shape,
            itemShape                = itemShape,
            tonalElevation           = tonalElevation,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppDrawerStyles — Pre-built factory presets
// ─────────────────────────────────────────────────────────────────────────────
object AppDrawerStyles {

    /** Default M3 Drawer — theme surface background, secondaryContainer indicator */
    @Composable
    fun default() = AppDrawerStyle.create()

    /**
     * Primary — uses primaryContainer for the selected active item.
     */
    @Composable
    fun primary() = AppDrawerStyle.create(
        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor      = MaterialTheme.colorScheme.onPrimaryContainer,
        selectedTextColor      = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    /**
     * Surface — slightly elevated surface variant color for container.
     */
    @Composable
    fun surface() = AppDrawerStyle.create(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )

    /**
     * Dark — permanently dark drawer sheet regardless of theme.
     */
    @Composable
    fun dark() = AppDrawerStyle.create(
        containerColor           = AppColors.Dark.Surface,
        selectedContainerColor   = AppColors.Brand.PurpleLight.copy(alpha = 0.2f),
        unselectedContainerColor = Color.Transparent,
        selectedIconColor        = AppColors.Brand.PurpleLight,
        unselectedIconColor      = AppColors.Dark.OnSurface.copy(alpha = 0.7f),
        selectedTextColor        = AppColors.White,
        unselectedTextColor      = AppColors.Dark.OnSurface.copy(alpha = 0.7f),
    )
}
