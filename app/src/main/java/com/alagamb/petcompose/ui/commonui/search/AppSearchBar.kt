package com.alagamb.petcompose.ui.commonui.search

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

// ─────────────────────────────────────────────────────────────────────────────
// AppSearchBar  (full-width, expands to overlay the screen)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Full-width search bar.  When expanded it overlays the screen and shows [content].
 *
 * Usage:
 * ```
 * var query by remember { mutableStateOf("") }
 * AppSearchBar(
 *     query         = query,
 *     onQueryChange = { query = it },
 *     onSearch      = { /* run search */ },
 *     placeholder   = "Search products..."
 * ) {
 *     // suggestions / history shown while expanded
 *     suggestions.forEach { s ->
 *         AppSearchSuggestionItem(text = s) { query = s }
 *     }
 * }
 * ```
 *
 * @param query          Current search text.
 * @param onQueryChange  Called on every keystroke.
 * @param onSearch       Called when the user submits (keyboard action).
 * @param modifier       Layout modifier for the search bar itself.
 * @param placeholder    Hint text shown when the bar is empty.
 * @param enabled        When false, the bar is dimmed and non-interactive.
 * @param colors         Override container, divider, and input field colors.
 * @param content        Composable suggestions / results shown while expanded.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    content: @Composable () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query            = query,
                onQueryChange    = onQueryChange,
                onSearch         = { onSearch(it); expanded = false },
                expanded         = expanded,
                onExpandedChange = { expanded = it },
                enabled          = enabled,
                placeholder      = { Text(placeholder) },
                leadingIcon      = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon     = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
            )
        },
        expanded         = expanded,
        onExpandedChange = { expanded = it },
        modifier         = modifier,
        colors           = colors,
    ) {
        content()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppDockedSearchBar  (anchored / embedded — does not overlay the screen)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Docked search bar — remains anchored in the layout (toolbar area).
 * Results expand downward inline without overlaying the rest of the screen.
 *
 * Prefer this over [AppSearchBar] when the search bar is permanently visible
 * (e.g. in a top app bar) and you want suggestions to appear below without
 * disrupting the rest of the UI.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDockedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search...",
    enabled: Boolean = true,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    content: @Composable () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }

    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query            = query,
                onQueryChange    = onQueryChange,
                onSearch         = { onSearch(it); expanded = false },
                expanded         = expanded,
                onExpandedChange = { expanded = it },
                enabled          = enabled,
                placeholder      = { Text(placeholder) },
                leadingIcon      = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon     = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
            )
        },
        expanded         = expanded,
        onExpandedChange = { expanded = it },
        modifier         = modifier,
        colors           = colors,
    ) {
        content()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppSearchSuggestionItem  (reusable suggestion row inside the search bar)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A standard suggestion row to use inside [AppSearchBar] or [AppDockedSearchBar] content.
 *
 * @param text     The suggestion label.
 * @param onClick  Called when the user taps the row.
 * @param isHistory When true a history icon is shown; false shows search icon.
 */
@Composable
fun AppSearchSuggestionItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isHistory: Boolean = true,
) {
    ListItem(
        headlineContent  = { Text(text) },
        leadingContent   = {
            Icon(
                imageVector        = if (isHistory) Icons.Default.History else Icons.Default.Search,
                contentDescription = null,
            )
        },
        colors           = ListItemDefaults.colors(containerColor = Color.Transparent),
        modifier         = modifier.clickable(onClick = onClick),
    )
}
