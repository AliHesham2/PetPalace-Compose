package com.alagamb.petcompose.ui.commonui.chips

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.ChipElevation
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector

// ─────────────────────────────────────────────────────────────────────────────
// AppAssistChip  (contextual action suggestions)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Assist chip — represents a smart / contextual action (e.g. "Open Maps", "Call").
 * Not selectable — each tap triggers an action.
 *
 * @param label       The chip label text.
 * @param onClick     Action triggered on tap.
 * @param leadingIcon Optional icon at the start.
 * @param trailingIcon Optional icon at the end.
 * @param shape       Corner shape — default is stadium (pill).
 * @param colors      Override container / label / icon colors ([ChipColors]).
 * @param elevation   Shadow levels per state ([ChipElevation]).
 */
@Composable
fun AppAssistChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    shape: Shape = AssistChipDefaults.shape,
    colors: ChipColors = AssistChipDefaults.assistChipColors(),
    elevation: ChipElevation? = AssistChipDefaults.assistChipElevation(),
) {
    AssistChip(
        onClick      = onClick,
        label        = { Text(label) },
        modifier     = modifier,
        enabled      = enabled,
        leadingIcon  = leadingIcon?.let { icon ->
            { Icon(icon, contentDescription = null, Modifier.size(AssistChipDefaults.IconSize)) }
        },
        trailingIcon = trailingIcon?.let { icon ->
            { Icon(icon, contentDescription = null, Modifier.size(AssistChipDefaults.IconSize)) }
        },
        shape        = shape,
        colors       = colors,
        elevation    = elevation,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppFilterChip  (toggleable — [SelectableChipColors])
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Filter chip — toggles between selected / unselected.
 *
 * ⚠️ Uses [SelectableChipColors] (not [ChipColors]) — use [FilterChipDefaults.filterChipColors].
 *
 * @param label          The chip label text.
 * @param selected       Current selected state.
 * @param onSelectedChange Callback with the new state.
 * @param showCheckIcon  When true a ✓ icon appears automatically while selected.
 * @param colors         [SelectableChipColors] override.
 * @param elevation      [SelectableChipElevation] override.
 */
@Composable
fun AppFilterChip(
    label: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    showCheckIcon: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    shape: Shape = FilterChipDefaults.shape,
    colors: SelectableChipColors = FilterChipDefaults.filterChipColors(),
    elevation: SelectableChipElevation? = FilterChipDefaults.filterChipElevation(),
) {
    FilterChip(
        selected = selected,
        onClick  = { onSelectedChange(!selected) },
        label    = { Text(label) },
        modifier = modifier,
        enabled  = enabled,
        leadingIcon = when {
            leadingIcon != null ->
                { { Icon(leadingIcon, null, Modifier.size(FilterChipDefaults.IconSize)) } }
            selected && showCheckIcon ->
                { { Icon(Icons.Default.Check, null, Modifier.size(FilterChipDefaults.IconSize)) } }
            else -> null
        },
        trailingIcon = trailingIcon?.let { icon ->
            { Icon(icon, contentDescription = null, Modifier.size(FilterChipDefaults.IconSize)) }
        },
        shape     = shape,
        colors    = colors,
        elevation = elevation,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppInputChip  (user-entered tag — [SelectableChipColors])
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Input chip — represents a piece of user input (tag, contact, search term).
 *
 * ⚠️ Uses [SelectableChipColors] (not [ChipColors]) — use [InputChipDefaults.inputChipColors].
 *
 * @param onDismiss Pass a lambda to show a close (×) icon; null = no icon.
 */
@Composable
fun AppInputChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onDismiss: (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    shape: Shape = InputChipDefaults.shape,
    colors: SelectableChipColors = InputChipDefaults.inputChipColors(),
    elevation: SelectableChipElevation? = InputChipDefaults.inputChipElevation(),
) {
    InputChip(
        selected     = selected,
        onClick      = onClick,
        label        = { Text(label) },
        modifier     = modifier,
        enabled      = enabled,
        leadingIcon  = leadingIcon?.let { icon ->
            { Icon(icon, contentDescription = null, Modifier.size(InputChipDefaults.IconSize)) }
        },
        trailingIcon = onDismiss?.let { dismiss ->
            {
                Icon(
                    imageVector        = Icons.Default.Check, // swap for Close when icons-extended loads
                    contentDescription = "Remove",
                    modifier           = Modifier.size(InputChipDefaults.IconSize),
                )
            }
        },
        shape     = shape,
        colors    = colors,
        elevation = elevation,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppSuggestionChip  (AI / search suggestions — [ChipColors])
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Suggestion chip — shows a search, autocomplete, or AI suggestion.
 */
@Composable
fun AppSuggestionChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    shape: Shape = SuggestionChipDefaults.shape,
    colors: ChipColors = SuggestionChipDefaults.suggestionChipColors(),
    elevation: ChipElevation? = SuggestionChipDefaults.suggestionChipElevation(),
) {
    SuggestionChip(
        onClick   = onClick,
        label     = { Text(label) },
        modifier  = modifier,
        enabled   = enabled,
        icon      = icon?.let {
            { Icon(it, contentDescription = null, Modifier.size(SuggestionChipDefaults.IconSize)) }
        },
        shape     = shape,
        colors    = colors,
        elevation = elevation,
    )
}
