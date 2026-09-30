package com.alagamb.petcompose.ui.commonui.pickers

import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

// ─────────────────────────────────────────────────────────────────────────────
// AppPickerStyle — reusable style for AppDatePickerDialog / AppTimePicker
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppDatePickerDialog(
//       state     = state,
//       onDismiss = { ... },
//       onConfirm = { millis -> ... },
//       colors    = AppPickerStyles.default().datePickerColors,
//   )
//   AppTimePicker(
//       state  = state,
//       colors = AppPickerStyles.branded().timePickerColors,
//   )
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppDatePickerDialog], [AppDateRangePickerDialog],
 * [AppInlineDatePicker], and time picker components.
 *
 * @param datePickerColors Colors for the [DatePicker] — selected day, today, range highlight, etc.
 * @param timePickerColors Colors for the [TimePicker] — clock face, selected hour/minute, etc.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Stable
data class AppPickerStyle(
    val datePickerColors: DatePickerColors,
    val timePickerColors: TimePickerColors,
)

// ── Pre-built Picker styles ────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
object AppPickerStyles {

    /**
     * Default M3 date/time picker — follows the theme color scheme automatically.
     * Selected day uses [primaryContainer]; today is outlined in [primary].
     */
    @Composable fun default() = AppPickerStyle(
        datePickerColors = DatePickerDefaults.colors(),
        timePickerColors = TimePickerDefaults.colors(),
    )

    /**
     * Branded — explicitly uses [primaryContainer] for the selected day background
     * and [onPrimaryContainer] for the text on the selected day.
     * Same as default() but made explicit for documentation clarity.
     */
    @Composable fun branded() = AppPickerStyle(
        datePickerColors = DatePickerDefaults.colors(
            selectedDayContainerColor    = MaterialTheme.colorScheme.primaryContainer,
            selectedDayContentColor      = MaterialTheme.colorScheme.onPrimaryContainer,
            todayDateBorderColor         = MaterialTheme.colorScheme.primary,
            selectedYearContainerColor   = MaterialTheme.colorScheme.primaryContainer,
            selectedYearContentColor     = MaterialTheme.colorScheme.onPrimaryContainer,
            dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            dayInSelectionRangeContentColor   = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        timePickerColors = TimePickerDefaults.colors(
            clockDialSelectedContentColor    = MaterialTheme.colorScheme.onPrimary,
            clockDialUnselectedContentColor  = MaterialTheme.colorScheme.onSurface,
            selectorColor                    = MaterialTheme.colorScheme.primary,
            containerColor                   = MaterialTheme.colorScheme.surfaceVariant,
            periodSelectorSelectedContainerColor   = MaterialTheme.colorScheme.primaryContainer,
            periodSelectorSelectedContentColor     = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
    )

    /**
     * Secondary — uses [secondaryContainer] for selected day and range.
     * Good for booking / availability pickers where secondary = available.
     */
    @Composable fun secondary() = AppPickerStyle(
        datePickerColors = DatePickerDefaults.colors(
            selectedDayContainerColor    = MaterialTheme.colorScheme.secondaryContainer,
            selectedDayContentColor      = MaterialTheme.colorScheme.onSecondaryContainer,
            todayDateBorderColor         = MaterialTheme.colorScheme.secondary,
            dayInSelectionRangeContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            dayInSelectionRangeContentColor   = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        timePickerColors = TimePickerDefaults.colors(
            selectorColor = MaterialTheme.colorScheme.secondary,
            periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            periodSelectorSelectedContentColor   = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    )
}
