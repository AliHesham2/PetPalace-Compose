package com.alagamb.petcompose.ui.commonui.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector

// AppButtonStyle is in the same package (buttons) — no import needed

// ─────────────────────────────────────────────────────────────────────────────
// AppButton  (Filled — highest emphasis)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Primary filled button.
 *
 * You can style it two ways — pick one:
 *
 * **1. Style object (recommended for reuse across screens)**
 * ```
 * AppButton("Login", onClick = {}, style = AppButtonStyles.primary())
 * AppButton("Delete", onClick = {}, style = AppButtonStyles.danger())
 * AppButton("Ghost", onClick = {}, style = AppButtonStyles.ghost())
 * // Custom one-off style:
 * val myStyle = AppButtonStyle.create(containerColor = Color(0xFF6200EA), shape = AppShape.Large)
 * AppButton("Custom", onClick = {}, style = myStyle)
 * ```
 *
 * **2. Individual parameters (quick overrides)**
 * ```
 * AppButton("Hi", onClick = {}, shape = AppShape.Medium, colors = ButtonDefaults.buttonColors(...))
 * ```
 *
 * @param text           Label shown inside the button.
 * @param onClick        Click callback.
 * @param modifier       Optional layout modifier.
 * @param enabled        When false the button is dimmed and non-interactive.
 * @param style          Optional [AppButtonStyle] — overrides [shape], [colors], and [border].
 * @param shape          Corner shape — ignored when [style] is provided.
 * @param colors         Colors — ignored when [style] is provided.
 * @param elevation      Shadow levels for each interaction state.
 * @param contentPadding Internal padding around the content.
 * @param leadingIcon    Optional icon placed before the text.
 * @param trailingIcon   Optional icon placed after the text.
 */
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: AppButtonStyle? = null,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    // Style object wins over individual params when provided
    val resolvedShape  = style?.shape  ?: shape
    val resolvedColors = style?.colors ?: colors
    val resolvedBorder = style?.border

    Button(
        onClick        = onClick,
        modifier       = modifier,
        enabled        = enabled,
        shape          = resolvedShape,
        colors         = resolvedColors,
        border         = resolvedBorder,
        elevation      = elevation,
        contentPadding = contentPadding,
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector        = leadingIcon,
                contentDescription = null,
                modifier           = Modifier.size(ButtonDefaults.IconSize),
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }
        Text(text)
        if (trailingIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Icon(
                imageVector        = trailingIcon,
                contentDescription = null,
                modifier           = Modifier.size(ButtonDefaults.IconSize),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppTonalButton  (Filled-Tonal — medium emphasis)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Tonal (secondary filled) button — uses [primaryContainer] by default.
 */
@Composable
fun AppTonalButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.filledTonalShape,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    elevation: ButtonElevation = ButtonDefaults.filledTonalButtonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    FilledTonalButton(
        onClick        = onClick,
        modifier       = modifier,
        enabled        = enabled,
        shape          = shape,
        colors         = colors,
        elevation      = elevation,
        contentPadding = contentPadding,
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }
        Text(text)
        if (trailingIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Icon(trailingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppOutlinedButton  (low emphasis, with border)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Outlined button — transparent background, [outline] colored border.
 */
@Composable
fun AppOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.outlinedShape,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    OutlinedButton(
        onClick        = onClick,
        modifier       = modifier,
        enabled        = enabled,
        shape          = shape,
        colors         = colors,
        contentPadding = contentPadding,
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }
        Text(text)
        if (trailingIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Icon(trailingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppElevatedButton  (filled with shadow, on surfaces)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Elevated button — use on surfaces where you still need visual separation.
 */
@Composable
fun AppElevatedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.elevatedShape,
    colors: ButtonColors = ButtonDefaults.elevatedButtonColors(),
    elevation: ButtonElevation = ButtonDefaults.elevatedButtonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    ElevatedButton(
        onClick        = onClick,
        modifier       = modifier,
        enabled        = enabled,
        shape          = shape,
        colors         = colors,
        elevation      = elevation,
        contentPadding = contentPadding,
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }
        Text(text)
        if (trailingIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Icon(trailingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppTextButton  (no fill, lowest emphasis)
// ─────────────────────────────────────────────────────────────────────────────
/**
 * Text-only button — least prominent action, inline dialogs, cards, etc.
 */
@Composable
fun AppTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.textShape,
    colors: ButtonColors = ButtonDefaults.textButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
) {
    TextButton(
        onClick        = onClick,
        modifier       = modifier,
        enabled        = enabled,
        shape          = shape,
        colors         = colors,
        contentPadding = contentPadding,
    ) {
        if (leadingIcon != null) {
            Icon(leadingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
        }
        Text(text)
        if (trailingIcon != null) {
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Icon(trailingIcon, contentDescription = null, Modifier.size(ButtonDefaults.IconSize))
        }
    }
}
