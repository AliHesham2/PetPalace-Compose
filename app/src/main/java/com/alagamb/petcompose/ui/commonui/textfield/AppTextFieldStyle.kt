package com.alagamb.petcompose.ui.commonui.textfield

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.customize.AppShape

// ─────────────────────────────────────────────────────────────────────────────
// AppTextFieldStyle — Style token for AppTextField / AppOutlinedTextField
//
// SAME PATTERN as AppButtonStyle / AppCardStyle / AppTopBarStyle.
// One object = full visual appearance of a text field.
//
// FIELDS:
//   containerColor        → background fill (filled variant)
//   focusedBorderColor    → indicator / outline color when focused
//   unfocusedBorderColor  → indicator / outline color when NOT focused
//   errorBorderColor      → indicator / outline color in error state
//   focusedLabelColor     → floating label color when focused
//   unfocusedLabelColor   → floating label color when NOT focused
//   errorLabelColor       → floating label color in error state
//   textColor             → typed text color
//   placeholderColor      → hint / placeholder text color
//   cursorColor           → cursor color
//   errorSupportingColor  → supporting/error text color below the field
//   shape                 → corner shape of the container
//
// HOW TO USE:
//   AppTextField(state = myState, label = "Email",
//       style = AppTextFieldStyles.default())
//   AppOutlinedTextField(state = myState, label = "Email",
//       style = AppTextFieldStyles.outlined())
//   AppTextField(state = myState, label = "Name",
//       style = AppTextFieldStyle.create(
//           containerColor   = Color(0xFF1A1A2E),
//           focusedBorderColor = AppColors.Brand.Purple,
//       ))
// ─────────────────────────────────────────────────────────────────────────────

@Immutable
data class AppTextFieldStyle(
    val containerColor      : Color,
    val focusedBorderColor  : Color,
    val unfocusedBorderColor: Color,
    val errorBorderColor    : Color,
    val focusedLabelColor   : Color,
    val unfocusedLabelColor : Color,
    val errorLabelColor     : Color,
    val textColor           : Color,
    val placeholderColor    : Color,
    val cursorColor         : Color,
    val errorSupportingColor: Color,
    val shape               : Shape,
) {
    /**
     * Converts to M3 [TextFieldColors] for use inside a [androidx.compose.material3.TextField].
     * Used internally by [AppTextField] and [AppOutlinedTextField].
     */
    @Composable
    fun toFilledColors(): TextFieldColors = TextFieldDefaults.colors(
        // Container
        focusedContainerColor   = containerColor,
        unfocusedContainerColor = containerColor,
        disabledContainerColor  = containerColor.copy(alpha = 0.38f),
        errorContainerColor     = containerColor,

        // Text
        focusedTextColor   = textColor,
        unfocusedTextColor = textColor,
        disabledTextColor  = textColor.copy(alpha = 0.38f),
        errorTextColor     = textColor,

        // Cursor
        cursorColor      = cursorColor,
        errorCursorColor = errorBorderColor,

        // Indicator (bottom line for filled variant)
        focusedIndicatorColor   = focusedBorderColor,
        unfocusedIndicatorColor = unfocusedBorderColor,
        disabledIndicatorColor  = unfocusedBorderColor.copy(alpha = 0.38f),
        errorIndicatorColor     = errorBorderColor,

        // Label
        focusedLabelColor   = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor  = unfocusedLabelColor.copy(alpha = 0.38f),
        errorLabelColor     = errorLabelColor,

        // Placeholder
        focusedPlaceholderColor   = placeholderColor,
        unfocusedPlaceholderColor = placeholderColor,
        disabledPlaceholderColor  = placeholderColor.copy(alpha = 0.38f),
        errorPlaceholderColor     = placeholderColor,

        // Supporting text
        focusedSupportingTextColor   = unfocusedLabelColor,
        unfocusedSupportingTextColor = unfocusedLabelColor,
        disabledSupportingTextColor  = unfocusedLabelColor.copy(alpha = 0.38f),
        errorSupportingTextColor     = errorSupportingColor,

        // Icons
        focusedLeadingIconColor   = focusedLabelColor,
        unfocusedLeadingIconColor = unfocusedLabelColor,
        errorLeadingIconColor     = errorBorderColor,
        focusedTrailingIconColor  = focusedLabelColor,
        unfocusedTrailingIconColor = unfocusedLabelColor,
        errorTrailingIconColor    = errorBorderColor,
    )

    /**
     * Converts to M3 [TextFieldColors] for use inside an
     * [androidx.compose.material3.OutlinedTextField].
     */
    @Composable
    fun toOutlinedColors(): TextFieldColors = TextFieldDefaults.colors(
        // Container (transparent for outlined)
        focusedContainerColor   = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor  = Color.Transparent,
        errorContainerColor     = Color.Transparent,

        // Text
        focusedTextColor   = textColor,
        unfocusedTextColor = textColor,
        disabledTextColor  = textColor.copy(alpha = 0.38f),
        errorTextColor     = textColor,

        // Cursor
        cursorColor      = cursorColor,
        errorCursorColor = errorBorderColor,

        // Outline border (used by OutlinedTextField instead of indicator)
        focusedIndicatorColor   = focusedBorderColor,
        unfocusedIndicatorColor = unfocusedBorderColor,
        disabledIndicatorColor  = unfocusedBorderColor.copy(alpha = 0.38f),
        errorIndicatorColor     = errorBorderColor,

        // Label
        focusedLabelColor   = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        errorLabelColor     = errorLabelColor,

        // Placeholder
        focusedPlaceholderColor   = placeholderColor,
        unfocusedPlaceholderColor = placeholderColor,
        errorPlaceholderColor     = placeholderColor,

        // Supporting text
        focusedSupportingTextColor   = unfocusedLabelColor,
        unfocusedSupportingTextColor = unfocusedLabelColor,
        errorSupportingTextColor     = errorSupportingColor,

        // Icons
        focusedLeadingIconColor    = focusedLabelColor,
        unfocusedLeadingIconColor  = unfocusedLabelColor,
        errorLeadingIconColor      = errorBorderColor,
        focusedTrailingIconColor   = focusedLabelColor,
        unfocusedTrailingIconColor = unfocusedLabelColor,
        errorTrailingIconColor     = errorBorderColor,
    )

    companion object {
        /**
         * Full custom style. All params have theme-color defaults so you only
         * need to specify what you want to override.
         */
        @Composable
        fun create(
            containerColor      : Color = MaterialTheme.colorScheme.surfaceContainerHighest,
            focusedBorderColor  : Color = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
            errorBorderColor    : Color = MaterialTheme.colorScheme.error,
            focusedLabelColor   : Color = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor : Color = MaterialTheme.colorScheme.onSurfaceVariant,
            errorLabelColor     : Color = MaterialTheme.colorScheme.error,
            textColor           : Color = MaterialTheme.colorScheme.onSurface,
            placeholderColor    : Color = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor         : Color = MaterialTheme.colorScheme.primary,
            errorSupportingColor: Color = MaterialTheme.colorScheme.error,
            shape               : Shape = AppShape.Medium,
        ) = AppTextFieldStyle(
            containerColor       = containerColor,
            focusedBorderColor   = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            errorBorderColor     = errorBorderColor,
            focusedLabelColor    = focusedLabelColor,
            unfocusedLabelColor  = unfocusedLabelColor,
            errorLabelColor      = errorLabelColor,
            textColor            = textColor,
            placeholderColor     = placeholderColor,
            cursorColor          = cursorColor,
            errorSupportingColor = errorSupportingColor,
            shape                = shape,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// AppTextFieldStyles — Pre-built factory presets
// ─────────────────────────────────────────────────────────────────────────────
object AppTextFieldStyles {

    /** Default M3 filled text field — surfaceContainerHighest bg, primary indicator */
    @Composable
    fun default() = AppTextFieldStyle.create()

    /**
     * Outlined — transparent bg, outline border instead of bottom indicator.
     * Pass this style to [AppOutlinedTextField] (not AppTextField).
     */
    @Composable
    fun outlined() = AppTextFieldStyle.create(
        containerColor = Color.Transparent,
    )

    /**
     * Search — rounded pill shape, no floating label, surfaceContainer bg.
     * Best used with [AppTextField] with placeholder only (no label).
     */
    @Composable
    fun search() = AppTextFieldStyle.create(
        containerColor       = MaterialTheme.colorScheme.surfaceContainer,
        focusedBorderColor   = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        shape                = AppShape.Pill,
    )

    /**
     * Brand — uses brand purple as the focused indicator / label color.
     * Good for onboarding, sign-up, and branded forms.
     */
    @Composable
    fun brand() = AppTextFieldStyle.create(
        focusedBorderColor = AppColors.Brand.Purple,
        focusedLabelColor  = AppColors.Brand.Purple,
        cursorColor        = AppColors.Brand.Purple,
    )

    /**
     * Success — green indicator signals a valid / accepted input.
     * Toggle between default() and success() based on validation result.
     */
    @Composable
    fun success() = AppTextFieldStyle.create(
        focusedBorderColor   = AppColors.Semantic.Success,
        unfocusedBorderColor = AppColors.Semantic.SuccessLight,
        focusedLabelColor    = AppColors.Semantic.Success,
        cursorColor          = AppColors.Semantic.Success,
    )

    /**
     * Danger — red indicator for invalid / rejected input.
     * Prefer using the [isError] + [errorMessage] params on AppTextField
     * over this preset — they also change the label and supporting text.
     */
    @Composable
    fun danger() = AppTextFieldStyle.create(
        focusedBorderColor   = AppColors.Semantic.Error,
        unfocusedBorderColor = AppColors.Semantic.ErrorLight,
        focusedLabelColor    = AppColors.Semantic.Error,
        cursorColor          = AppColors.Semantic.Error,
    )

    /**
     * Dark — forced dark container regardless of theme.
     * Use inside dark cards, bottom sheets, or modals.
     */
    @Composable
    fun dark() = AppTextFieldStyle.create(
        containerColor       = AppColors.Dark.SurfaceHigh,
        focusedBorderColor   = AppColors.Brand.PurpleLight,
        unfocusedBorderColor = AppColors.Dark.OnSurfaceMuted,
        focusedLabelColor    = AppColors.Brand.PurpleLight,
        unfocusedLabelColor  = AppColors.Dark.OnSurfaceMuted,
        textColor            = AppColors.Dark.OnSurface,
        placeholderColor     = AppColors.Dark.OnSurfaceMuted,
        cursorColor          = AppColors.Brand.PurpleLight,
        errorSupportingColor = AppColors.Semantic.ErrorLight,
    )
}
