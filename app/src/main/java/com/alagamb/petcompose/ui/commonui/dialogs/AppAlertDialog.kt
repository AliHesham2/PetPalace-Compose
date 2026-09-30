package com.alagamb.petcompose.ui.commonui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.alagamb.petcompose.ui.commonui.buttons.AppButton
import com.alagamb.petcompose.ui.commonui.buttons.AppOutlinedButton
import com.alagamb.petcompose.ui.commonui.buttons.AppTextButton

// ─────────────────────────────────────────────────────────────────────────────
// AppAlertDialog  (standard Material 3 alert dialog)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Standard Material 3 [AlertDialog] with title, body text, optional icon,
 * a confirm button, and an optional dismiss button.
 *
 * Usage:
 * ```
 * var show by remember { mutableStateOf(false) }
 * if (show) {
 *     AppAlertDialog(
 *         title   = "Delete?",
 *         text    = "This action cannot be undone.",
 *         onConfirm = { show = false },
 *         onDismiss = { show = false }
 *     )
 * }
 * ```
 *
 * @param title            Dialog title text.
 * @param text             Body / supporting text.
 * @param onDismissRequest Called when the user taps outside or presses Back.
 * @param confirmText      Label for the confirm button — defaults to "OK".
 * @param dismissText      Label for the dismiss button — pass null to hide it.
 * @param onConfirm        Action for the confirm button.
 * @param onDismiss        Action for the dismiss button.
 * @param icon             Optional leading icon above the title.
 * @param shape            Corner shape — default is M3 28 dp rounded.
 * @param containerColor   Dialog background color.
 * @param iconContentColor Tint for [icon].
 * @param titleContentColor Color of the title text.
 * @param textContentColor  Color of the body text.
 * @param properties        Window-level dialog properties.
 */
@Composable
fun AppAlertDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "OK",
    dismissText: String? = "Cancel",
    onDismiss: (() -> Unit)? = null,
    icon: ImageVector? = null,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        onDismissRequest  = onDismissRequest,
        confirmButton     = {
            AppTextButton(text = confirmText, onClick = onConfirm)
        },
        modifier          = modifier,
        dismissButton     = if (dismissText != null) {
            { AppTextButton(text = dismissText, onClick = { onDismiss?.invoke() ?: onDismissRequest() }) }
        } else null,
        icon              = icon?.let { { Icon(it, contentDescription = null) } },
        title             = { Text(title) },
        text              = { Text(text) },
        shape             = shape,
        containerColor    = containerColor,
        iconContentColor  = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor  = textContentColor,
        tonalElevation    = tonalElevation,
        properties        = properties,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// AppConfirmDialog  (filled confirm + outlined cancel — for destructive actions)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Alert dialog with a filled **confirm** button and an outlined **cancel** button.
 * Use for destructive / irreversible actions (delete, clear, reset).
 *
 * @param confirmButtonColors Override the confirm button colors (e.g. error scheme).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppConfirmDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    confirmText: String = "Confirm",
    cancelText: String = "Cancel",
    icon: ImageVector? = null,
    confirmButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    shape: Shape = RoundedCornerShape(28.dp),
    containerColor: Color = AlertDialogDefaults.containerColor,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier         = modifier,
        properties       = DialogProperties(),
    ) {
        Surface(
            shape         = shape,
            color         = containerColor,
            tonalElevation = AlertDialogDefaults.TonalElevation,
        ) {
            Column(
                modifier            = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (icon != null) {
                    Icon(
                        imageVector        = icon,
                        contentDescription = null,
                        modifier           = Modifier.size(24.dp),
                        tint               = AlertDialogDefaults.iconContentColor,
                    )
                    Spacer(Modifier.height(16.dp))
                }
                Text(
                    text  = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = AlertDialogDefaults.titleContentColor,
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text  = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AlertDialogDefaults.textContentColor,
                )
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    AppOutlinedButton(
                        text     = cancelText,
                        onClick  = onDismissRequest,
                        modifier = Modifier.weight(1f),
                    )
                    AppButton(
                        text     = confirmText,
                        onClick  = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors   = confirmButtonColors,
                    )
                }
            }
        }
    }
}
