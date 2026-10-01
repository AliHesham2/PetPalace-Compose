package com.alagamb.petcompose.ui.commonui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass

// ─────────────────────────────────────────────────────────────────────────────
// AppAdaptiveNavigationScaffold — Adaptive Navigation Component (Phone & Tablet/Fold)
//
// WHAT IT DOES:
//   Automatically switches between:
//   - Material 3 NavigationBar at the bottom on Compact screens (Phones).
//   - Material 3 NavigationRail on the side on Medium/Expanded screens (Tablets/Foldables).
//
// SUPPORTS:
//   - Vertical alignment control in NavigationRail (Top, Center, Bottom).
//   - Reuses the exact same List<BottomNavItem> and AppBottomBarStyle tokens.
//   - Supports badge counts, custom icons, and full styling tokens.
// ─────────────────────────────────────────────────────────────────────────────

enum class RailAlignment {
    Top,
    Center,
    Bottom
}

/**
 * Adaptive navigation scaffold that renders a bottom bar on phones and a navigation rail on wide screens.
 *
 * @param items         List of [BottomNavItem] displayed in the navigation bar/rail.
 * @param currentRoute  Current active destination route for selection highlighting.
 * @param onNavigate    Callback triggered when a navigation item is selected.
 * @param modifier      Modifier applied to the root container.
 * @param topBar        Top app bar composable.
 * @param style         Styling token for colors, elevation, and indicator (defaults to theme).
 * @param railAlignment Vertical alignment of items within the navigation rail (defaults to Center).
 * @param content       Screen content with Scaffold [PaddingValues].
 */
@Composable
fun AppAdaptiveNavigationScaffold(
    items: List<BottomNavItem>,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    style: AppBottomBarStyle = AppBottomBarStyles.default(),
    railAlignment: RailAlignment = RailAlignment.Center,
    content: @Composable (PaddingValues) -> Unit
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isCompactHeight = !adaptiveInfo.windowSizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
    )
    val isMediumOrExpandedWidth = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )
    val isPhoneLandscape = isCompactHeight && isMediumOrExpandedWidth

    val layoutType = when {
        isPhoneLandscape -> NavigationSuiteType.NavigationRail
        else -> NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    }

    Box(modifier = modifier.fillMaxSize()) {
        NavigationSuiteScaffoldLayout(
            navigationSuite = {
                when (layoutType) {
                    NavigationSuiteType.NavigationRail -> {
                        NavigationRail(
                            modifier = Modifier.fillMaxHeight(),
                            containerColor = style.containerColor
                        ) {
                            if (railAlignment == RailAlignment.Center || railAlignment == RailAlignment.Bottom) {
                                Spacer(modifier = Modifier.weight(1f))
                            }

                            val itemSpacing = if (isCompactHeight) 8.dp else 16.dp

                            items.forEachIndexed { index, item ->
                                if (index > 0) {
                                    Spacer(modifier = Modifier.height(itemSpacing))
                                }
                                val isSelected = currentRoute == item.route
                                NavigationRailItem(
                                    selected = isSelected,
                                    onClick = { onNavigate(item.route) },
                                    colors = style.toRailItemColors(),
                                    alwaysShowLabel = true,
                                    label = {
                                        Text(
                                            text = item.label,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    },
                                    icon = {
                                        if (item.badgeCount != null) {
                                            BadgedBox(
                                                badge = {
                                                    Badge {
                                                        Text(
                                                            text = item.badgeCount.toString(),
                                                            style = MaterialTheme.typography.labelSmall
                                                        )
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                    contentDescription = item.label
                                                )
                                            }
                                        } else {
                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.label
                                            )
                                        }
                                    }
                                )
                            }

                            if (railAlignment == RailAlignment.Center || railAlignment == RailAlignment.Top) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                    else -> {
                        AppBottomBar(
                            items = items,
                            currentRoute = currentRoute,
                            onNavigate = onNavigate,
                            style = style
                        )
                    }
                }
            },
            layoutType = layoutType
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = topBar
            ) { innerPadding ->
                content(innerPadding)
            }
        }
    }
}
