package com.alagamb.petcompose.ui.commonui.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

// ─────────────────────────────────────────────────────────────────────────────
// AppCustomDialog  (blank canvas — full UI control)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A fully custom dialog that gives you a blank [Surface] canvas.
 * Use this when [AppAlertDialog] doesn't fit your design.
 *
 * The [content] lambda receives a [ColumnScope]-like composable slot — you own
 * the entire interior layout.
 *
 * Usage:
 * ```
 * AppCustomDialog(onDismissRequest = { showDialog = false }) {
 *     Column(modifier = Modifier.padding(24.dp)) {
 *         Text("My Custom Dialog")
 *         Spacer(Modifier.height(16.dp))
 *         AppButton("Close", onClick = { showDialog = false })
 *     }
 * }
 * ```
 *
 * @param onDismissRequest Called when the user taps outside or presses Back.
 * @param modifier         Modifier applied to the [Surface] (use to set width, padding).
 * @param shape            Corner shape — default is 24 dp rounded.
 * @param containerColor   Surface/background color — default is M3 [surface].
 * @param tonalElevation   Tonal surface overlay elevation.
 * @param widthFraction    Fraction of screen width used (0f..1f). Default 0.9f.
 * @param properties       Window-level dialog properties.
 * @param content          Your completely custom composable content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCustomDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    widthFraction: Float = 0.9f,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties       = properties,
    ) {
        Surface(
            modifier      = modifier
                .fillMaxWidth(widthFraction)
                .wrapContentHeight(),
            shape         = shape,
            color         = containerColor,
            tonalElevation = tonalElevation,
        ) {
            content()
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppBottomSheetDialog  (dialog that looks like a bottom sheet, anchored bottom)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * A custom dialog that appears anchored to the bottom of the screen,
 * with only top corners rounded — like a bottom sheet but as a [Dialog].
 *
 * Note: For proper bottom sheet behavior with swipe-to-dismiss, prefer
 * [ModalBottomSheet] from Material 3.  This variant is useful when you need
 * a lightweight dialog that *looks* like a bottom sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBottomDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    tonalElevation: Dp = 6.dp,
    content: @Composable () -> Unit,
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        properties       = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier        = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Surface(
                modifier      = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape         = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color         = containerColor,
                tonalElevation = tonalElevation,
            ) {
                content()
            }
        }
    }
}
