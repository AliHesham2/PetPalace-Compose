package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppRadioButton  (bare radio button, no label)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A plain radio button without a label.
 *
 * @param selected  Whether this radio button is the selected option.
 * @param onClick   Click callback — set to null when using [AppRadioGroup].
 * @param colors    Override selected / unselected / disabled colors.
 */
@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
) {
    RadioButton(
        selected  = selected,
        onClick   = onClick,
        modifier  = modifier,
        enabled   = enabled,
        colors    = colors,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppRadioGroup  (vertical list of labeled radio options)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Vertical radio button group.  Each row is fully tappable (better touch target).
 *
 * Usage:
 * ```
 * var picked by remember { mutableStateOf("Option A") }
 * AppRadioGroup(
 *     options       = listOf("Option A", "Option B", "Option C"),
 *     selectedOption = picked,
 *     onOptionSelected = { picked = it }
 * )
 * ```
 *
 * @param options          List of option labels.
 * @param selectedOption   The label of the currently selected option.
 * @param onOptionSelected Callback with the newly selected label.
 * @param modifier         Modifier applied to the outer [Column].
 * @param enabled          When false all rows are dimmed and non-interactive.
 * @param colors           Radio button colors applied to each item.
 */
@Composable
fun AppRadioGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors = RadioButtonDefaults.colors(),
) {
    Column(
        modifier  = modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected  = (option == selectedOption),
                        enabled   = enabled,
                        role      = Role.RadioButton,
                        onClick   = { onOptionSelected(option) },
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected  = (option == selectedOption),
                    onClick   = null,   // handled by Row.selectable
                    enabled   = enabled,
                    colors    = colors,
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text  = option,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (enabled) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                )
            }
        }
    }
}
