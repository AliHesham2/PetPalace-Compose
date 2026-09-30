package com.alagamb.petcompose.ui.commonui.pickers

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alagamb.petcompose.ui.commonui.buttons.AppTextButton

// ─────────────────────────────────────────────────────────────────────────────
// AppDatePickerDialog  (calendar in a dialog — single date)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A date picker wrapped in a dialog.
 *
 * Usage:
 * ```
 * var show by remember { mutableStateOf(false) }
 * val state = rememberDatePickerState()
 *
 * if (show) {
 *     AppDatePickerDialog(
 *         state     = state,
 *         onDismiss = { show = false },
 *         onConfirm = { millis ->
 *             // use millis
 *             show = false
 *         }
 *     )
 * }
 * ```
 *
 * @param state         [DatePickerState] — holds selected date, display mode, year range.
 * @param onDismiss     Called when the user cancels / taps outside.
 * @param onConfirm     Called with the selected date millis (null if nothing selected).
 * @param confirmText   Label for the confirm button.
 * @param dismissText   Label for the cancel button.
 * @param showModeToggle When true an icon lets the user toggle between calendar and input.
 * @param colors        Override all date picker colors (days, selected day, today, etc.).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePickerDialog(
    state: DatePickerState = rememberDatePickerState(),
    onDismiss: () -> Unit,
    onConfirm: (selectedMillis: Long?) -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    showModeToggle: Boolean = true,
    colors: DatePickerColors = DatePickerDefaults.colors(),
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton    = {
            AppTextButton(
                text    = confirmText,
                onClick = { onConfirm(state.selectedDateMillis) },
            )
        },
        dismissButton    = {
            AppTextButton(text = dismissText, onClick = onDismiss)
        },
        modifier = modifier,
    ) {
        DatePicker(
            state          = state,
            showModeToggle = showModeToggle,
            colors         = colors,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppDateRangePickerDialog  (calendar in a dialog — start + end dates)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A date **range** picker wrapped in a dialog.
 *
 * Usage:
 * ```
 * var show by remember { mutableStateOf(false) }
 * val state = rememberDateRangePickerState()
 *
 * if (show) {
 *     AppDateRangePickerDialog(
 *         state     = state,
 *         onDismiss = { show = false },
 *         onConfirm = { start, end ->
 *             // use start / end millis
 *             show = false
 *         }
 *     )
 * }
 * ```
 *
 * @param state     [DateRangePickerState] — holds start / end millis.
 * @param onConfirm Called with (startMillis, endMillis) — either may be null.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateRangePickerDialog(
    state: DateRangePickerState = rememberDateRangePickerState(),
    onDismiss: () -> Unit,
    onConfirm: (startMillis: Long?, endMillis: Long?) -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    colors: DatePickerColors = DatePickerDefaults.colors(),
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton    = {
            AppTextButton(
                text    = confirmText,
                onClick = {
                    onConfirm(
                        state.selectedStartDateMillis,
                        state.selectedEndDateMillis,
                    )
                },
            )
        },
        dismissButton    = {
            AppTextButton(text = dismissText, onClick = onDismiss)
        },
        modifier = modifier,
    ) {
        DateRangePicker(state = state, colors = colors)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppInlineDatePicker  (embedded in screen — no dialog wrapper)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A date picker rendered inline on the screen (not in a dialog).
 * Use when you have enough vertical space or want the picker always visible.
 *
 * @param initialDisplayMode [DisplayMode.Picker] (calendar grid) or [DisplayMode.Input] (text field).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInlineDatePicker(
    state: DatePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
    ),
    modifier: Modifier = Modifier,
    // null = DatePicker renders its own default title.
    // Pass a lambda to fully customise: title = { Text("Pick a date") }
    title: @Composable (() -> Unit)? = {
        DatePickerDefaults.DatePickerTitle(
            displayMode = state.displayMode,
            modifier    = Modifier,
        )
    },
    showModeToggle: Boolean = true,
    colors: DatePickerColors = DatePickerDefaults.colors(),
) {
    DatePicker(
        state          = state,
        modifier       = modifier,
        title          = title,
        showModeToggle = showModeToggle,
        colors         = colors,
    )
}
