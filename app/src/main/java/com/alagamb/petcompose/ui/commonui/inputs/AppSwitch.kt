package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// AppSwitch  (bare switch, no label)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Bare switch — no label.
 *
 * @param checked           Current on/off state.
 * @param onCheckedChange   State change callback.
 * @param modifier          Optional layout modifier.
 * @param enabled           When false the switch is dimmed and non-interactive.
 * @param thumbContent      Optional icon shown inside the thumb (use [SwitchDefaults.IconSize]).
 * @param colors            Override track, thumb, icon, and border colors for
 *                          both checked and unchecked states.
 */
@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: @Composable (() -> Unit)? = null,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    Switch(
        checked         = checked,
        onCheckedChange = onCheckedChange,
        modifier        = modifier,
        enabled         = enabled,
        thumbContent    = thumbContent,
        colors          = colors,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppLabeledSwitch  (label on the left, switch on the right — settings row)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A full-width row with a [label] (and optional [description]) on the left
 * and a switch on the right.  Perfect for settings screens.
 *
 * Usage:
 * ```
 * var darkMode by remember { mutableStateOf(false) }
 * AppLabeledSwitch(
 *     label       = "Dark Mode",
 *     description = "Switch the app to dark theme",
 *     checked     = darkMode,
 *     onCheckedChange = { darkMode = it }
 * )
 * ```
 *
 * @param label          Primary text displayed to the left.
 * @param description    Optional secondary text below the label.
 * @param checked        Current switch state.
 * @param onCheckedChange Callback with the new state.
 * @param thumbContent   Optional icon inside the thumb.
 * @param colors         Override switch colors.
 */
@Composable
fun AppLabeledSwitch(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    enabled: Boolean = true,
    thumbContent: @Composable (() -> Unit)? = null,
    colors: SwitchColors = SwitchDefaults.colors(),
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        // Text block
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text  = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            )
            if (description != null) {
                Text(
                    text  = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Switch(
            checked         = checked,
            onCheckedChange = onCheckedChange,
            enabled         = enabled,
            thumbContent    = thumbContent,
            colors          = colors,
        )
    }
}
