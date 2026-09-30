package com.alagamb.petcompose.ui.commonui.dialogs

import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

// ─────────────────────────────────────────────────────────────────────────────
// AppDialogStyle — reusable style for AppAlertDialog / AppConfirmDialog
// ─────────────────────────────────────────────────────────────────────────────
//
// Usage:
//   AppAlertDialog(
//       title     = "Delete?",
//       text      = "This cannot be undone.",
//       onConfirm = { ... },
//       onDismiss = { ... },
//       style     = AppDialogStyles.danger(),
//   )
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Bundles visual tokens for [AppAlertDialog] and [AppConfirmDialog].
 *
 * @param containerColor   Background color of the dialog surface.
 * @param iconColor        Tint applied to the leading icon.
 * @param titleColor       Color of the title text.
 * @param textColor        Color of the body text.
 * @param shape            Corner shape of the dialog surface.
 * @param tonalElevation   Tonal surface elevation in dp.
 */
@Stable
data class AppDialogStyle(
    val containerColor: Color,
    val iconColor: Color,
    val titleColor: Color,
    val textColor: Color,
    val shape: Shape,
    val tonalElevation: Dp,
) {
    companion object {
        /**
         * Factory — create a fully custom dialog style.
         * Only specify what you want to override.
         */
        @Composable
        fun create(
            containerColor: Color = AlertDialogDefaults.containerColor,
            iconColor: Color = AlertDialogDefaults.iconContentColor,
            titleColor: Color = AlertDialogDefaults.titleContentColor,
            textColor: Color = AlertDialogDefaults.textContentColor,
            shape: Shape = AlertDialogDefaults.shape,
            tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
        ) = AppDialogStyle(
            containerColor = containerColor,
            iconColor      = iconColor,
            titleColor     = titleColor,
            textColor      = textColor,
            shape          = shape,
            tonalElevation = tonalElevation,
        )
    }
}

// ── Pre-built Dialog styles ────────────────────────────────────────────────

object AppDialogStyles {

    /** Default M3 alert dialog — [surfaceContainerHigh] bg. */
    @Composable fun default() = AppDialogStyle.create()

    /**
     * Danger dialog — icon and title tinted in [error] color.
     * Use for destructive confirmations (delete, reset, clear).
     */
    @Composable fun danger() = AppDialogStyle.create(
        iconColor  = MaterialTheme.colorScheme.error,
        titleColor = MaterialTheme.colorScheme.error,
    )

    /**
     * Info dialog — icon tinted with [primary] color.
     * Use for informational notices.
     */
    @Composable fun info() = AppDialogStyle.create(
        iconColor  = MaterialTheme.colorScheme.primary,
        titleColor = MaterialTheme.colorScheme.onSurface,
    )

    /**
     * Success dialog — icon tinted with tertiary (green in default theme).
     * Use for completed / success confirmations.
     */
    @Composable fun success() = AppDialogStyle.create(
        iconColor  = MaterialTheme.colorScheme.tertiary,
        titleColor = MaterialTheme.colorScheme.onSurface,
    )

    /**
     * Warning dialog — icon tinted with [secondaryContainer] on secondary.
     * Use for caution notices (e.g. "Are you sure?").
     */
    @Composable fun warning() = AppDialogStyle.create(
        iconColor  = MaterialTheme.colorScheme.secondary,
        titleColor = MaterialTheme.colorScheme.onSurface,
    )

    /**
     * Tinted dialog — [primaryContainer] bg for a branded look.
     * Use for onboarding tips, feature announcements.
     */
    @Composable fun tinted() = AppDialogStyle.create(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        iconColor      = MaterialTheme.colorScheme.onPrimaryContainer,
        titleColor     = MaterialTheme.colorScheme.onPrimaryContainer,
        textColor      = MaterialTheme.colorScheme.onPrimaryContainer,
    )
}
