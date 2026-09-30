package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// AppRadioStyle — reusable style for RadioButton / AppRadioGroup
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppRadioButton(selected = true, onClick = {},
//       colors = AppRadioStyles.success().colors)
//   AppRadioGroup(options = options, selectedOption = picked,
//       colors = AppRadioStyles.default().colors)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppRadioButton] and [AppRadioGroup].
 *
 * @param colors Selected / unselected / disabled radio button colors.
 */
@Stable
data class AppRadioStyle(
    val colors: RadioButtonColors,
) {
    companion object {
        /**
         * Factory — create a fully custom radio style.
         * Only specify what you want to override.
         */
        @Composable
        fun create(
            selectedColor: Color = MaterialTheme.colorScheme.primary,
            unselectedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledSelectedColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
            disabledUnselectedColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        ) = AppRadioStyle(
            colors = RadioButtonDefaults.colors(
                selectedColor          = selectedColor,
                unselectedColor        = unselectedColor,
                disabledSelectedColor  = disabledSelectedColor,
                disabledUnselectedColor = disabledUnselectedColor,
            ),
        )
    }
}

// ── Pre-built Radio styles ─────────────────────────────────────────────────

object AppRadioStyles {

    /** Default M3 radio — follows theme primary. */
    @Composable fun default() = AppRadioStyle(
        colors = RadioButtonDefaults.colors(),
    )

    /** Success / green radio. */
    @Composable fun success() = AppRadioStyle(
        colors = RadioButtonDefaults.colors(
            selectedColor = Color(0xFF2E7D32),
        ),
    )

    /** Warning / amber radio. */
    @Composable fun warning() = AppRadioStyle(
        colors = RadioButtonDefaults.colors(
            selectedColor = Color(0xFFF9A825),
        ),
    )

    /** Danger / error radio. */
    @Composable fun danger() = AppRadioStyle(
        colors = RadioButtonDefaults.colors(
            selectedColor = MaterialTheme.colorScheme.error,
        ),
    )

    /** Secondary radio — uses secondary color. */
    @Composable fun secondary() = AppRadioStyle(
        colors = RadioButtonDefaults.colors(
            selectedColor = MaterialTheme.colorScheme.secondary,
        ),
    )
}
