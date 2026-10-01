package com.alagamb.petcompose.ui.commonui.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppDrawer — Reusable M3 Navigation Drawer component
//
// WHAT IT DOES:
//   Provides a customizable, template-based Navigation Drawer for the app.
//   Supports standard DrawerNavItem items with icons, badges, custom header/footer,
//   and styling via AppDrawerStyle.
//
// HOW TO USE:
//   AppModalDrawer(
//       drawerState = drawerState,
//       items = drawerItems,
//       currentRoute = currentRoute,
//       onNavigate = { route -> ... },
//       gesturesEnabled = true,
//       style = AppDrawerStyles.default()
//   ) {
//       Scaffold(...) { ... }
//   }
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Data item representing a single entry inside the drawer menu.
 *
 * @param route       Unique route key for this drawer destination.
 * @param label       Text label displayed on the item.
 * @param icon        Leading icon for the item.
 * @param badgeCount  Optional badge count displayed on the right.
 */
data class DrawerNavItem(
    val route     : String,
    val label     : String,
    val icon      : ImageVector,
    val badgeCount: Int? = null,
)

/**
 * Material 3 Modal Navigation Drawer wrapper with template-driven items and custom styling.
 *
 * @param drawerState     State of the drawer (open / closed).
 * @param items           List of [DrawerNavItem] to render.
 * @param currentRoute    Active route to highlight the selected item.
 * @param onNavigate      Invoked when an item is clicked with its route.
 * @param modifier        Modifier for the outer drawer container.
 * @param gesturesEnabled Whether dragging from the screen edge opens the drawer.
 * @param title           Default header text when [header] is not provided.
 * @param header          Optional custom header slot (replaces default title).
 * @param footer          Optional footer slot (pinned to bottom of sheet).
 * @param style           Visual styling token ([AppDrawerStyle]).
 * @param content         The main screen content wrapped by the drawer.
 */
@Composable
fun AppModalDrawer(
    drawerState    : DrawerState,
    items          : List<DrawerNavItem>,
    currentRoute   : String,
    onNavigate     : (String) -> Unit,
    modifier       : Modifier = Modifier,
    sheetModifier  : Modifier = Modifier.fillMaxWidth(0.75f),
    gesturesEnabled: Boolean = true,
    title          : String? = "Petify",
    header         : (@Composable () -> Unit)? = null,
    footer         : (@Composable () -> Unit)? = null,
    style          : AppDrawerStyle = AppDrawerStyles.default(),
    content        : @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        drawerState     = drawerState,
        gesturesEnabled = gesturesEnabled,
        modifier        = modifier,
        drawerContent   = {
            AppDrawerSheet(
                items        = items,
                currentRoute = currentRoute,
                onNavigate   = onNavigate,
                modifier     = sheetModifier,
                title        = title,
                header       = header,
                footer       = footer,
                style        = style,
            )
        },
        content         = content
    )
}

/**
 * Standalone Drawer Sheet content composable if you want to use it
 * directly inside an existing ModalDrawerSheet or custom drawer container.
 */
@Composable
fun AppDrawerSheet(
    modifier    : Modifier = Modifier,
    items       : List<DrawerNavItem>,
    currentRoute: String,
    onNavigate  : (String) -> Unit,
    title       : String? = "Petify",
    header      : (@Composable () -> Unit)? = null,
    footer      : (@Composable () -> Unit)? = null,
    style       : AppDrawerStyle = AppDrawerStyles.default(),
) {
    ModalDrawerSheet(
        modifier             = modifier,
        drawerContainerColor = style.containerColor,
        drawerTonalElevation = style.tonalElevation,
        drawerShape          = style.shape,
    ) {
        // Header
        if (header != null) {
            header()
        } else if (!title.isNullOrEmpty()) {
            Text(
                text       = title,
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.primary,
                modifier   = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))
        }

        // Nav items
        items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationDrawerItem(
                label    = {
                    Text(
                        text       = item.label,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    )
                },
                icon     = {
                    Icon(
                        imageVector        = item.icon,
                        contentDescription = item.label,
                    )
                },
                badge    = if (item.badgeCount != null) {
                    {
                        Badge {
                            Text(text = item.badgeCount.toString())
                        }
                    }
                } else null,
                selected = isSelected,
                onClick  = { onNavigate(item.route) },
                shape    = style.itemShape,
                colors   = style.toItemColors(),
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        // Optional footer slot
        if (footer != null) {
            Spacer(modifier = Modifier.weight(1f))
            footer()
        }
    }
}
