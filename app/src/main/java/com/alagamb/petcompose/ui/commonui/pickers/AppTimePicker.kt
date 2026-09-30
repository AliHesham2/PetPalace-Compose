package com.alagamb.petcompose.ui.commonui.pickers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.alagamb.petcompose.ui.commonui.buttons.AppIconButton
import com.alagamb.petcompose.ui.commonui.buttons.AppTextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule

// ─────────────────────────────────────────────────────────────────────────────
// AppTimePickerDialog  (clock picker in a dialog — with mode toggle)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A [TimePicker] (clock dial) wrapped in a custom [Dialog].
 *
 * Material 3 does NOT ship a `TimePickerDialog` composable, so we wrap it
 * ourselves using a [Dialog] + [Surface].
 *
 * The dialog includes a **mode toggle** button (clock ↔ keyboard input).
 *
 * Usage:
 * ```
 * var show by remember { mutableStateOf(false) }
 * val state = rememberTimePickerState(initialHour = 9, initialMinute = 0)
 *
 * if (show) {
 *     AppTimePickerDialog(
 *         state     = state,
 *         onDismiss = { show = false },
 *         onConfirm = { hour, minute -> show = false }
 *     )
 * }
 * ```
 *
 * @param state          [TimePickerState] — holds selected hour, minute, is24Hour.
 * @param onDismiss      Called when the user cancels / taps outside.
 * @param onConfirm      Called with the selected (hour, minute).
 * @param confirmText    Label for the confirm button.
 * @param dismissText    Label for the cancel button.
 * @param showModeToggle When true a toggle icon switches between dial and input.
 * @param colors         Override all time picker colors.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTimePickerDialog(
    state: TimePickerState = rememberTimePickerState(initialHour = 12, initialMinute = 0),
    onDismiss: () -> Unit,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    showModeToggle: Boolean = true,
    colors: TimePickerColors = TimePickerDefaults.colors(),
) {
    // Toggle between dial and keyboard input
    var isDialMode by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties       = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier      = modifier
                .fillMaxWidth(0.9f),
            shape         = RoundedCornerShape(28.dp),
            color         = AlertDialogDefaults.containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier            = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Title
                Text(
                    text     = "Select time",
                    style    = MaterialTheme.typography.labelMedium,
                    color    = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                )

                // Picker — dial or keyboard
                if (isDialMode) {
                    TimePicker(
                        state      = state,
                        colors     = colors,
                        layoutType = TimePickerLayoutType.Vertical,
                    )
                } else {
                    TimeInput(state = state, colors = colors)
                }

                Spacer(Modifier.height(8.dp))

                // Footer: mode toggle  +  Cancel / OK
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    if (showModeToggle) {
                        AppIconButton(
                            icon               = if (isDialMode) Icons.Default.Edit else Icons.Default.Schedule,
                            contentDescription = if (isDialMode) "Switch to keyboard" else "Switch to clock",
                            onClick            = { isDialMode = !isDialMode },
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }

                    Row(horizontalArrangement = Arrangement.End) {
                        AppTextButton(text = dismissText, onClick = onDismiss)
                        AppTextButton(
                            text    = confirmText,
                            onClick = { onConfirm(state.hour, state.minute) },
                        )
                    }
                }
            }
        }
    }
}
