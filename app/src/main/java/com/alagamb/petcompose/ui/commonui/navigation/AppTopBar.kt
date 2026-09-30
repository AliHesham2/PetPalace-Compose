package com.alagamb.petcompose.ui.commonui.navigation

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.alagamb.petcompose.ui.commonui.buttons.AppIconButton
import com.alagamb.petcompose.ui.commonui.customize.AppColors

// ─────────────────────────────────────────────────────────────────────────────
// AppTopBarStyle — Style token for AppTopBar
//
// SAME PATTERN as AppButtonStyle / AppCardStyle — one object = full appearance.
//
// FIELDS:
//   containerColor       → background of the entire top bar
//   titleContentColor    → title text color
//   navigationIconColor  → back arrow / hamburger icon color
//   actionIconColor      → action icons (right side) color
//   scrolledContainerColor → color when content is scrolled under the bar
//
// HOW TO USE:
//   AppTopBar(title = "Screen", style = AppTopBarStyles.default())
//   AppTopBar(title = "Screen", style = AppTopBarStyles.primary())
//   AppTopBar(title = "Screen", style = AppTopBarStyle.create(
//       containerColor = Color(0xFF1A1A2E),
//       titleContentColor = Color.White,
//   ))
// ─────────────────────────────────────────────────────────────────────────────

@Immutable
data class AppTopBarStyle(
    val containerColor              : Color,
    val titleContentColor           : Color,
    val navigationIconColor         : Color,
    val actionIconColor             : Color,
    val scrolledContainerColor      : Color,
    val navigationIconContainerColor: Color = Color.Transparent,
) {
    /** Converts to M3 [TopAppBarColors] required by [TopAppBar]. */
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun toM3Colors(): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor        = containerColor,
        titleContentColor     = titleContentColor,
        navigationIconContentColor = navigationIconColor,
        actionIconContentColor     = actionIconColor,
        scrolledContainerColor     = scrolledContainerColor,
    )

    companion object {
        @Composable
        fun create(
            containerColor              : Color = MaterialTheme.colorScheme.surface,
            titleContentColor           : Color = MaterialTheme.colorScheme.onSurface,
            navigationIconColor         : Color = MaterialTheme.colorScheme.onSurface,
            actionIconColor             : Color = MaterialTheme.colorScheme.onSurfaceVariant,
            scrolledContainerColor      : Color = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContainerColor: Color = Color.Transparent,
        ) = AppTopBarStyle(
            containerColor               = containerColor,
            titleContentColor            = titleContentColor,
            navigationIconColor          = navigationIconColor,
            actionIconColor              = actionIconColor,
            scrolledContainerColor       = scrolledContainerColor,
            navigationIconContainerColor = navigationIconContainerColor,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppTopBarStyles — Pre-built factory presets
// ─────────────────────────────────────────────────────────────────────────────
object AppTopBarStyles {

    /** Default M3 TopAppBar — surface background, theme text colors */
    @Composable
    fun default() = AppTopBarStyle.create()

    /**
     * Primary — uses primary color as background.
     * Content (title, icons) uses onPrimary color for contrast.
     * Good for branded headers.
     */
    @Composable
    fun primary() = AppTopBarStyle.create(
        containerColor        = MaterialTheme.colorScheme.primary,
        titleContentColor     = MaterialTheme.colorScheme.onPrimary,
        navigationIconColor   = MaterialTheme.colorScheme.onPrimary,
        actionIconColor       = MaterialTheme.colorScheme.onPrimary,
        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
    )

    /**
     * Secondary — uses secondary container as background.
     * Softer branded look.
     */
    @Composable
    fun secondary() = AppTopBarStyle.create(
        containerColor        = MaterialTheme.colorScheme.secondaryContainer,
        titleContentColor     = MaterialTheme.colorScheme.onSecondaryContainer,
        navigationIconColor   = MaterialTheme.colorScheme.onSecondaryContainer,
        actionIconColor       = MaterialTheme.colorScheme.onSecondaryContainer,
        scrolledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
    )

    /**
     * Transparent — no background. Works over gradients or images.
     * Combine with a gradient in the Scaffold background.
     */
    @Composable
    fun transparent() = AppTopBarStyle.create(
        containerColor        = Color.Transparent,
        scrolledContainerColor = Color.Transparent,
    )

    /**
     * Dark — forced dark background regardless of theme.
     * Use for screens with a permanently dark header.
     */
    @Composable
    fun dark() = AppTopBarStyle.create(
        containerColor        = AppColors.Dark.Surface,
        titleContentColor     = AppColors.Dark.OnSurface,
        navigationIconColor   = AppColors.Dark.OnSurface,
        actionIconColor       = AppColors.Dark.OnSurface.copy(alpha = 0.7f),
        scrolledContainerColor = AppColors.Dark.Surface,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppTopBar — Reusable TopAppBar composable
//
// Wraps M3 TopAppBar with our style system — same pattern as AppButton/AppCard.
//
// PARAMETERS:
//   title           → screen title string
//   style           → AppTopBarStyle (defaults to AppTopBarStyles.default())
//   navigationIcon  → icon to show on the left (e.g. back arrow) — null = hidden
//   onNavigationClick → called when navigationIcon is tapped
//   actions         → composable lambda for right-side action icons
//   scrollBehavior  → from TopAppBarDefaults.enterAlwaysScrollBehavior() etc.
//
// EXAMPLES:
//   // Simple title
//   AppTopBar(title = "Home")
//
//   // Branded primary bar with back arrow
//   AppTopBar(
//       title          = "Details",
//       style          = AppTopBarStyles.primary(),
//       navigationIcon = Icons.Default.ArrowBack,
//       onNavigationClick = { navController.popBackStack() },
//   )
//
//   // With action icons on the right
//   AppTopBar(title = "Search") {
//       AppIconButton(Icons.Default.Search, "Search", onClick = { })
//       AppIconButton(Icons.Default.MoreVert, "Menu", onClick = { })
//   }
// ─────────────────────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title                           : String,
    modifier                        : Modifier              = Modifier,
    style                           : AppTopBarStyle        = AppTopBarStyles.default(),
    navigationIcon                  : ImageVector?          = null,
    navigationIconContentDescription: String?               = "Navigation icon",
    navigationIconContainerColor    : Color                 = style.navigationIconContainerColor,
    navigationIconContentColor      : Color                 = style.navigationIconColor,
    onNavigationClick               : () -> Unit            = {},
    scrollBehavior                  : TopAppBarScrollBehavior? = null,
    centerTitle                     : Boolean               = false,
    navigationIconSlot              : (@Composable () -> Unit)? = null,
    actions                         : @Composable () -> Unit = {},
) {
    val navIconComposable: @Composable () -> Unit = {
        if (navigationIconSlot != null) {
            navigationIconSlot()
        } else if (navigationIcon != null) {
            AppIconButton(
                icon               = navigationIcon,
                contentDescription = navigationIconContentDescription,
                onClick            = onNavigationClick,
                colors             = IconButtonDefaults.iconButtonColors(
                    containerColor = navigationIconContainerColor,
                    contentColor   = navigationIconContentColor,
                ),
            )
        }
    }

    if (centerTitle) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text  = title,
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            modifier       = modifier,
            colors         = style.toM3Colors(),
            scrollBehavior = scrollBehavior,
            navigationIcon = navIconComposable,
            actions        = { actions() },
        )
    } else {
        TopAppBar(
            title = {
                Text(
                    text  = title,
                    style = MaterialTheme.typography.titleLarge,
                )
            },
            modifier       = modifier,
            colors         = style.toM3Colors(),
            scrollBehavior = scrollBehavior,
            navigationIcon = navIconComposable,
            actions        = { actions() },
        )
    }
}
