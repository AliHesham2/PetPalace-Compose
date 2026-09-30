package com.alagamb.petcompose.ui.commonui.search

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// AppSearchBarStyle — reusable style for AppSearchBar / AppDockedSearchBar
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppSearchBar(
//       query         = query,
//       onQueryChange = { query = it },
//       onSearch      = { ... },
//       colors        = AppSearchBarStyles.tinted().colors,
//   )
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppSearchBar] and [AppDockedSearchBar].
 *
 * @param colors [SearchBarColors] — controls the container, divider, and
 *               the inner input field's text/hint/icon colors.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Stable
data class AppSearchBarStyle(
    val colors: SearchBarColors,
) {
    companion object {
        /**
         * Factory — create a custom search bar style.
         * Only specify what you want to override.
         *
         * @param containerColor      Background of the search bar pill / container.
         * @param dividerColor        The hairline divider shown when expanded.
         * @param inputTextColor      Color of typed text.
         * @param inputHintColor      Color of the placeholder / hint text.
         * @param inputLeadingIconColor  Search icon tint.
         * @param inputTrailingIconColor Clear icon tint.
         */
        @Composable
        fun create(
            containerColor: Color = SearchBarDefaults.colors().containerColor,
            dividerColor: Color = SearchBarDefaults.colors().dividerColor,
            inputTextColor: Color = MaterialTheme.colorScheme.onSurface,
            inputHintColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            inputLeadingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            inputTrailingIconColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
        ) = AppSearchBarStyle(
            colors = SearchBarDefaults.colors(
                containerColor = containerColor,
                dividerColor   = dividerColor,
                inputFieldColors = TextFieldDefaults.colors(
                    focusedTextColor    = inputTextColor,
                    unfocusedTextColor  = inputTextColor,
                    focusedPlaceholderColor  = inputHintColor,
                    unfocusedPlaceholderColor = inputHintColor,
                    focusedLeadingIconColor   = inputLeadingIconColor,
                    unfocusedLeadingIconColor = inputLeadingIconColor,
                    focusedTrailingIconColor  = inputTrailingIconColor,
                    unfocusedTrailingIconColor = inputTrailingIconColor,
                ),
            ),
        )
    }
}

// ── Pre-built SearchBar styles ─────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
object AppSearchBarStyles {

    /**
     * Default M3 search bar — [surfaceContainerHigh] container.
     * Follows the theme automatically.
     */
    @Composable fun default() = AppSearchBarStyle(
        colors = SearchBarDefaults.colors(),
    )

    /**
     * Tinted — [surfaceVariant] container for a subtle filled look.
     * Slightly more visible on light backgrounds.
     */
    @Composable fun tinted() = AppSearchBarStyle.create(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )

    /**
     * Transparent — no background. Use with edge-to-edge or when the
     * search bar sits on a colored/gradient surface.
     */
    @Composable fun transparent() = AppSearchBarStyle.create(
        containerColor = Color.Transparent,
        dividerColor   = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
    )

    /**
     * Primary — [primaryContainer] background.
     * Use for branded hero search bars at the top of discovery screens.
     */
    @Composable fun primary() = AppSearchBarStyle.create(
        containerColor       = MaterialTheme.colorScheme.primaryContainer,
        inputTextColor       = MaterialTheme.colorScheme.onPrimaryContainer,
        inputHintColor       = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
        inputLeadingIconColor  = MaterialTheme.colorScheme.onPrimaryContainer,
        inputTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}
