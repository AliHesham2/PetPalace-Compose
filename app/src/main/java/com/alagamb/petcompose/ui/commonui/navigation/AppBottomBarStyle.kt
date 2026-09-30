package com.alagamb.petcompose.ui.commonui.navigation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRailItemColors
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.alagamb.petcompose.ui.commonui.customize.AppColors

// ─────────────────────────────────────────────────────────────────────────────
// AppBottomBarStyle — Style token for AppBottomBar
//
// SAME PATTERN as AppButtonStyle / AppCardStyle — one object = full appearance.
//
// FIELDS:
//   containerColor  → background color of the entire NavigationBar
//   indicatorColor  → pill highlight behind the selected icon
//   selectedIconColor     → icon color when selected
//   unselectedIconColor   → icon color when NOT selected
//   selectedLabelColor    → label text color when selected
//   unselectedLabelColor  → label text color when NOT selected
//   tonalElevation  → elevation for tonal surface color (M3 tonal elevation)
//
// HOW TO USE:
//   AppBottomBar(items = ..., currentRoute = ..., onNavigate = ...,
//       style = AppBottomBarStyles.default())       // uses theme colors
//       style = AppBottomBarStyles.dark())           // forced dark surface
//       style = AppBottomBarStyles.primary())        // primary container bg
//       style = AppBottomBarStyle.create(            // full custom
//           containerColor = Color(0xFF1A1A2E),
//           indicatorColor = Color(0xFF7B61FF),
//       )
// ─────────────────────────────────────────────────────────────────────────────

@Immutable
data class AppBottomBarStyle(
    val containerColor      : Color,
    val indicatorColor      : Color,
    val selectedIconColor   : Color,
    val unselectedIconColor : Color,
    val selectedLabelColor  : Color,
    val unselectedLabelColor: Color,
    val tonalElevation      : Dp,
) {
    // Converts to the M3 NavigationBarItemColors required by NavigationBarItem
    @Composable
    fun toItemColors(): NavigationBarItemColors = NavigationBarItemDefaults.colors(
        selectedIconColor    = selectedIconColor,
        unselectedIconColor  = unselectedIconColor,
        selectedTextColor    = selectedLabelColor,
        unselectedTextColor  = unselectedLabelColor,
        indicatorColor       = indicatorColor,
    )

    // Converts to the M3 NavigationRailItemColors required by NavigationRailItem
    @Composable
    fun toRailItemColors(): NavigationRailItemColors = NavigationRailItemDefaults.colors(
        selectedIconColor    = selectedIconColor,
        unselectedIconColor  = unselectedIconColor,
        selectedTextColor    = selectedLabelColor,
        unselectedTextColor  = unselectedLabelColor,
        indicatorColor       = indicatorColor,
    )

    companion object {
        /**
         * Fully custom style. Only specify what you want to override —
         * all other colors fall back to the current MaterialTheme.
         */
        @Composable
        fun create(
            containerColor      : Color = MaterialTheme.colorScheme.surface,
            indicatorColor      : Color = MaterialTheme.colorScheme.secondaryContainer,
            selectedIconColor   : Color = MaterialTheme.colorScheme.onSecondaryContainer,
            unselectedIconColor : Color = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedLabelColor  : Color = MaterialTheme.colorScheme.onSurface,
            unselectedLabelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            tonalElevation      : Dp   = NavigationBarDefaults.Elevation,
        ) = AppBottomBarStyle(
            containerColor       = containerColor,
            indicatorColor       = indicatorColor,
            selectedIconColor    = selectedIconColor,
            unselectedIconColor  = unselectedIconColor,
            selectedLabelColor   = selectedLabelColor,
            unselectedLabelColor = unselectedLabelColor,
            tonalElevation       = tonalElevation,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppBottomBarStyles — Pre-built factory presets
// ─────────────────────────────────────────────────────────────────────────────
object AppBottomBarStyles {

    /** Default M3 NavigationBar — surface bg, secondaryContainer indicator */
    @Composable
    fun default() = AppBottomBarStyle.create()

    /**
     * Primary — uses primaryContainer as indicator highlight.
     * Feels more branded / colorful.
     */
    @Composable
    fun primary() = AppBottomBarStyle.create(
        indicatorColor    = MaterialTheme.colorScheme.primaryContainer,
        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )

    /**
     * Surface — slightly elevated surface color (M3 tonal surface).
     * Subtle and neutral.
     */
    @Composable
    fun surface() = AppBottomBarStyle.create(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )

    /**
     * Dark — forced dark background regardless of theme mode.
     * Use for apps that want a permanently dark nav bar.
     */
    @Composable
    fun dark() = AppBottomBarStyle.create(
        containerColor       = AppColors.Dark.Surface,
        indicatorColor       = AppColors.Brand.PurpleLight,
        selectedIconColor    = AppColors.White,
        unselectedIconColor  = AppColors.Dark.OnSurface.copy(alpha = 0.6f),
        selectedLabelColor   = AppColors.Dark.OnSurface,
        unselectedLabelColor = AppColors.Dark.OnSurface.copy(alpha = 0.6f),
    )

    /**
     * Transparent — no background. Content shows behind the bar.
     * Combine with edge-to-edge + a background gradient.
     */
    @Composable
    fun transparent() = AppBottomBarStyle.create(
        containerColor       = Color.Transparent,
        unselectedIconColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        unselectedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    )
}
