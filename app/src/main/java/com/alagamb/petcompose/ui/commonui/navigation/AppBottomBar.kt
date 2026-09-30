package com.alagamb.petcompose.ui.commonui.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

// ─────────────────────────────────────────────────────────────────────────────
// AppBottomBar — Reusable M3 NavigationBar component
//
// WHAT IT DOES:
//   Renders a Material 3 NavigationBar with items you define via BottomNavItem.
//   Highlights the current destination and supports optional badge counts.
//
// HOW TO USE:
//   1. Define your items as a list of BottomNavItem (usually in the nav layer)
//   2. Pass the currentRoute from navController.currentBackStackEntryAsState()
//   3. Pass onNavigate to handle item clicks → call navController.navigate(route)
//
// EXAMPLE:
//   val items = listOf(
//       BottomNavItem(route = "home", label = "Home",
//           selectedIcon   = Icons.Filled.Home,
//           unselectedIcon = Icons.Outlined.Home),
//       BottomNavItem(route = "profile", label = "Profile",
//           selectedIcon   = Icons.Filled.Person,
//           unselectedIcon = Icons.Outlined.Person,
//           badgeCount     = 3),          // shows red badge with "3"
//   )
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Represents one item in the bottom navigation bar.
 *
 * @param route         Navigation route string — must match a NavHost composable route.
 * @param label         Short label shown below the icon (keep ≤ 10 chars).
 * @param selectedIcon  Icon shown when this item is the current destination (filled).
 * @param unselectedIcon Icon shown when not selected (outlined/unfilled).
 * @param badgeCount    Optional number to show in a red badge (null = no badge).
 */
data class BottomNavItem(
    val route         : String,
    val label         : String,
    val selectedIcon  : ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount    : Int? = null,
)

/**
 * Material 3 NavigationBar wrapper.
 *
 * @param items        List of [BottomNavItem] to display.
 * @param currentRoute The active route string — compare to each item's route to highlight.
 * @param onNavigate   Called with the item's route when the user taps a nav item.
 * @param style        Appearance — colors, indicator, elevation. Defaults to theme colors.
 *                     Use [AppBottomBarStyles] for presets or [AppBottomBarStyle.Companion.create] for custom.
 */
@Composable
fun AppBottomBar(
    items       : List<BottomNavItem>,
    currentRoute: String,
    onNavigate  : (String) -> Unit,
    style       : AppBottomBarStyle = AppBottomBarStyles.default(),
) {
    NavigationBar(
        containerColor = style.containerColor,
        tonalElevation = style.tonalElevation,
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                selected = isSelected,
                onClick  = { onNavigate(item.route) },
                colors   = style.toItemColors(),
                // Label — always visible (not hidden when unselected)
                label        = { Text(item.label, style = MaterialTheme.typography.labelMedium) },
                alwaysShowLabel = true,

                // Icon — with optional badge
                icon = {
                    if (item.badgeCount != null) {
                        // Badged icon (notification count)
                        BadgedBox(
                            badge = {
                                Badge {
                                    Text(
                                        text  = item.badgeCount.toString(),
                                        style = MaterialTheme.typography.labelSmall,
                                    )
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                            )
                        }
                    } else {
                        // Plain icon
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                        )
                    }
                }
            )
        }
    }
}
