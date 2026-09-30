package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// AppCheckboxStyle — reusable style for Checkbox / AppLabeledCheckbox / AppTriStateCheckbox
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppCheckbox(checked = true, onCheckedChange = {},
//       colors = AppCheckboxStyles.success().colors)
//   AppLabeledCheckbox(label = "Accept Terms", checked = agreed,
//       colors = AppCheckboxStyles.default().colors)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppCheckbox], [AppLabeledCheckbox], and [AppTriStateCheckbox].
 *
 * @param colors Checked / unchecked / indeterminate / disabled checkbox colors.
 */
@Stable
data class AppCheckboxStyle(
    val colors: CheckboxColors,
) {
    companion object {
        /**
         * Factory — create a fully custom checkbox style.
         * Only specify what you want to override.
         */
        @Composable
        fun create(
            checkedColor: Color = MaterialTheme.colorScheme.primary,
            uncheckedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            checkmarkColor: Color = MaterialTheme.colorScheme.onPrimary,
            disabledCheckedColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledUncheckedColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledIndeterminateColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        ) = AppCheckboxStyle(
            colors = CheckboxDefaults.colors(
                checkedColor               = checkedColor,
                uncheckedColor             = uncheckedColor,
                checkmarkColor             = checkmarkColor,
                disabledCheckedColor       = disabledCheckedColor,
                disabledUncheckedColor     = disabledUncheckedColor,
                disabledIndeterminateColor = disabledIndeterminateColor,
            ),
        )
    }
}

// ── Pre-built Checkbox styles ──────────────────────────────────────────────

object AppCheckboxStyles {

    /** Default M3 checkbox — follows theme primary. */
    @Composable fun default() = AppCheckboxStyle(
        colors = CheckboxDefaults.colors(),
    )

    /** Success / green checkbox. */
    @Composable fun success() = AppCheckboxStyle(
        colors = CheckboxDefaults.colors(
            checkedColor   = Color(0xFF2E7D32),
            checkmarkColor = Color.White,
        ),
    )

    /** Warning / amber checkbox. */
    @Composable fun warning() = AppCheckboxStyle(
        colors = CheckboxDefaults.colors(
            checkedColor   = Color(0xFFF9A825),
            checkmarkColor = Color.White,
        ),
    )

    /** Danger / error checkbox. */
    @Composable fun danger() = AppCheckboxStyle(
        colors = CheckboxDefaults.colors(
            checkedColor   = MaterialTheme.colorScheme.error,
            checkmarkColor = MaterialTheme.colorScheme.onError,
        ),
    )

    /** Secondary checkbox — uses secondary color. */
    @Composable fun secondary() = AppCheckboxStyle(
        colors = CheckboxDefaults.colors(
            checkedColor   = MaterialTheme.colorScheme.secondary,
            checkmarkColor = MaterialTheme.colorScheme.onSecondary,
        ),
    )
}
