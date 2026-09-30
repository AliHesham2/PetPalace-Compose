package com.alagamb.petcompose.ui.commonui.inputs

import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

// ─────────────────────────────────────────────────────────────────────────────
// AppSwitchStyle — reusable style for Switch / AppLabeledSwitch
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppSwitch(checked = on, onCheckedChange = { on = it },
//       colors = AppSwitchStyles.success().colors)
//   AppLabeledSwitch(label = "Dark Mode", checked = dark,
//       colors = AppSwitchStyles.default().colors)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppSwitch] and [AppLabeledSwitch].
 *
 * @param colors All Switch color states (track, thumb, icon, border) for
 *               checked and unchecked — use [SwitchDefaults.colors] as base.
 */
@Stable
data class AppSwitchStyle(
    val colors: SwitchColors,
) {
    companion object {
        /**
         * Factory — create a fully custom switch style.
         * Only specify what you want to override; all others fall back to M3 defaults.
         */
        @Composable
        fun create(
            checkedTrackColor: Color = MaterialTheme.colorScheme.primary,
            checkedThumbColor: Color = MaterialTheme.colorScheme.onPrimary,
            checkedIconColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
            checkedBorderColor: Color = Color.Transparent,
            uncheckedTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
            uncheckedThumbColor: Color = MaterialTheme.colorScheme.outline,
            uncheckedIconColor: Color = MaterialTheme.colorScheme.surfaceVariant,
            uncheckedBorderColor: Color = MaterialTheme.colorScheme.outline,
            disabledCheckedTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            disabledCheckedThumbColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.38f),
            disabledUncheckedTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f),
            disabledUncheckedThumbColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        ) = AppSwitchStyle(
            colors = SwitchDefaults.colors(
                checkedTrackColor           = checkedTrackColor,
                checkedThumbColor           = checkedThumbColor,
                checkedIconColor            = checkedIconColor,
                checkedBorderColor          = checkedBorderColor,
                uncheckedTrackColor         = uncheckedTrackColor,
                uncheckedThumbColor         = uncheckedThumbColor,
                uncheckedIconColor          = uncheckedIconColor,
                uncheckedBorderColor        = uncheckedBorderColor,
                disabledCheckedTrackColor   = disabledCheckedTrackColor,
                disabledCheckedThumbColor   = disabledCheckedThumbColor,
                disabledUncheckedTrackColor = disabledUncheckedTrackColor,
                disabledUncheckedThumbColor = disabledUncheckedThumbColor,
            ),
        )
    }
}

// ── Pre-built Switch styles ────────────────────────────────────────────────

object AppSwitchStyles {

    /** Default M3 switch — follows theme primary color. */
    @Composable fun default() = AppSwitchStyle(
        colors = SwitchDefaults.colors(),
    )

    /** Green / success switch — checked track is green. */
    @Composable fun success() = AppSwitchStyle(
        colors = SwitchDefaults.colors(
            checkedTrackColor   = Color(0xFF2E7D32),
            checkedThumbColor   = Color.White,
            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    )

    /** Warning switch — checked track is amber. */
    @Composable fun warning() = AppSwitchStyle(
        colors = SwitchDefaults.colors(
            checkedTrackColor = Color(0xFFF9A825),
            checkedThumbColor = Color.White,
        ),
    )

    /** Danger switch — checked track is [error]. */
    @Composable fun danger() = AppSwitchStyle(
        colors = SwitchDefaults.colors(
            checkedTrackColor = MaterialTheme.colorScheme.error,
            checkedThumbColor = MaterialTheme.colorScheme.onError,
        ),
    )

    /** Secondary switch — uses secondaryContainer. */
    @Composable fun secondary() = AppSwitchStyle(
        colors = SwitchDefaults.colors(
            checkedTrackColor = MaterialTheme.colorScheme.secondary,
            checkedThumbColor = MaterialTheme.colorScheme.onSecondary,
        ),
    )
}
