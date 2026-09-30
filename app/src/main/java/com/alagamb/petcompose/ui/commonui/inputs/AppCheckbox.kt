package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppCheckbox  (bare checkbox, no label)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A plain checkbox without a label.
 *
 * @param checked         Current checked state.
 * @param onCheckedChange State change callback.
 * @param modifier        Optional layout modifier.
 * @param enabled         When false the checkbox is dimmed and non-interactive.
 * @param colors          Override checked / unchecked / disabled colors.
 */
@Composable
fun AppCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    Checkbox(
        checked         = checked,
        onCheckedChange = onCheckedChange,
        modifier        = modifier,
        enabled         = enabled,
        colors          = colors,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppLabeledCheckbox  (checkbox + text label, whole row is tappable)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Checkbox with a text label.  The entire row is clickable (better touch target).
 *
 * @param label           Text shown beside the checkbox.
 * @param checked         Current checked state.
 * @param onCheckedChange State change callback.
 * @param modifier        Optional layout modifier applied to the Row.
 * @param enabled         When false the row is dimmed and non-interactive.
 * @param colors          Override checkbox colors.
 */
@Composable
fun AppLabeledCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    Row(
        modifier = modifier.toggleable(
            value    = checked,
            enabled  = enabled,
            role     = Role.Checkbox,
            onValueChange = onCheckedChange,
        ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Checkbox(
            checked         = checked,
            onCheckedChange = null,   // handled by Row's toggleable
            enabled         = enabled,
            colors          = colors,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text  = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (enabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppTriStateCheckbox  (On / Off / Indeterminate — parent checkbox for groups)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Tri-state checkbox supporting [ToggleableState.Indeterminate].
 * Useful as a "select all" parent when a group of checkboxes is partially selected.
 *
 * @param state   [ToggleableState.On], [ToggleableState.Off], or [ToggleableState.Indeterminate].
 * @param onClick Click callback — update [state] inside it.
 */
@Composable
fun AppTriStateCheckbox(
    state: ToggleableState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors(),
) {
    TriStateCheckbox(
        state    = state,
        onClick  = onClick,
        modifier = modifier,
        enabled  = enabled,
        colors   = colors,
    )
}
