package com.alagamb.petcompose.ui.commonui.textfield

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

// ─────────────────────────────────────────────────────────────────────────────
// AppTextField Package — Reusable M3 Text Fields (State-Based API)
//
// WHY STATE-BASED (not value/onValueChange)?
//   The old pattern:
//     var text by remember { mutableStateOf("") }
//     TextField(value = text, onValueChange = { text = it })
//
//   The new pattern (since Compose 1.7 / BOM 2023.09.00):
//     val state = rememberTextFieldState()       ← or TextFieldState()
//     TextField(state = state)
//     // To read the value: state.text.toString()
//
//   Benefits of state-based:
//     • No intermediate string allocation on every keystroke
//     • Cursor position / selection is properly preserved
//     • InputTransformation + OutputTransformation built in
//     • Works with SecureTextField for passwords (no custom VisualTransformation)
//
// COMPOSABLES IN THIS FILE:
//   AppTextField          → M3 filled text field (state-based)
//   AppOutlinedTextField  → M3 outlined text field (state-based)
//   AppPasswordTextField  → Password field with eye-toggle visibility
//
// USAGE EXAMPLES:
//
//   // 1. Simple filled text field
//   val emailState = rememberTextFieldState()
//   AppTextField(state = emailState, label = "Email",
//       placeholder = "you@example.com",
//       leadingIcon = Icons.Outlined.Email)
//
//   // 2. Outlined with validation
//   var isError by remember { mutableStateOf(false) }
//   AppOutlinedTextField(
//       state        = usernameState,
//       label        = "Username",
//       isError      = isError,
//       errorMessage = "Username must be 3+ characters",
//       style        = AppTextFieldStyles.outlined(),
//   )
//
//   // 3. Password field
//   val passwordState = rememberTextFieldState()
//   AppPasswordTextField(state = passwordState, label = "Password")
//
//   // 4. Search field (no label, rounded, clear indicator)
//   AppTextField(state = searchState, placeholder = "Search...",
//       leadingIcon = Icons.Outlined.Search,
//       style       = AppTextFieldStyles.search())
//
//   // 5. Read the current value
//   val text = state.text.toString()
// ─────────────────────────────────────────────────────────────────────────────

// ── AppTextField — M3 Filled Text Field ─────────────────────────────────────
/**
 * Filled M3 TextField using the state-based API.
 *
 * The state ([TextFieldState]) is the single source of truth for the text content.
 * Create it with [rememberTextFieldState] and read it with `state.text.toString()`.
 *
 * @param state            Text field state — create with [rememberTextFieldState].
 * @param modifier         Layout modifier.
 * @param label            Floating label text (null = no label).
 * @param placeholder      Hint text shown when the field is empty (null = none).
 * @param leadingIcon      Icon on the left side of the field (null = none).
 * @param trailingIcon     Icon on the right side of the field (null = none).
 * @param trailingContent  Custom composable replacing [trailingIcon] (null = none).
 * @param supportingText   Helper text shown below the field when not in error state.
 * @param isError          When true, the field turns red and shows [errorMessage].
 * @param errorMessage     Error text shown below the field when [isError] is true.
 * @param enabled          When false, the field is dimmed and non-interactive.
 * @param readOnly         When true, text cannot be edited (but can be selected).
 * @param singleLine       When true, field stays on one line (prevents newlines).
 * @param maxLength        Optional maximum character count (uses [InputTransformation]).
 * @param keyboardOptions  Keyboard type and IME action.
 * @param inputTransformation Custom [InputTransformation] (overrides [maxLength]).
 * @param style            Appearance token. Defaults to [AppTextFieldStyles.default].
 */
@Composable
fun AppTextField(
    state               : TextFieldState,
    modifier            : Modifier                  = Modifier,
    label               : String?                   = null,
    placeholder         : String?                   = null,
    leadingIcon         : ImageVector?              = null,
    trailingIcon        : ImageVector?              = null,
    trailingContent     : @Composable (() -> Unit)? = null,
    supportingText      : String?                   = null,
    isError             : Boolean                   = false,
    errorMessage        : String?                   = null,
    enabled             : Boolean                   = true,
    readOnly            : Boolean                   = false,
    singleLine          : Boolean                   = true,
    maxLength           : Int?                      = null,
    keyboardOptions     : KeyboardOptions           = KeyboardOptions.Default,
    inputTransformation : InputTransformation?      = null,
    style               : AppTextFieldStyle = AppTextFieldStyles.default(),
) {
    TextField(
        state    = state,
        modifier = modifier,
        enabled  = enabled,
        readOnly = readOnly,
        colors   = style.toFilledColors(),
        shape    = style.shape,
        isError  = isError,

        lineLimits = if (singleLine) TextFieldLineLimits.SingleLine
                     else            TextFieldLineLimits.MultiLine(),

        // Input transformation — maxLength if set, otherwise caller-provided or null
        inputTransformation = inputTransformation
            ?: maxLength?.let { InputTransformation.maxLength(it) },

        keyboardOptions = keyboardOptions,

        label = label?.let { { Text(it, style = MaterialTheme.typography.bodyMedium) } },

        placeholder = placeholder?.let {
            { Text(it, style = MaterialTheme.typography.bodyMedium) }
        },

        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null) }
        },

        trailingIcon = trailingContent ?: trailingIcon?.let {
            { Icon(it, contentDescription = null) }
        },

        // Supporting / error text
        supportingText = when {
            isError && errorMessage != null ->
                { { Text(errorMessage, style = MaterialTheme.typography.labelSmall) } }
            supportingText != null ->
                { { Text(supportingText, style = MaterialTheme.typography.labelSmall) } }
            else -> null
        },
    )
}

// ── AppOutlinedTextField — M3 Outlined Text Field ───────────────────────────
/**
 * Outlined M3 TextField using the state-based API.
 *
 * Same parameters as [AppTextField] — pass [AppTextFieldStyles.outlined] as style.
 */
@Composable
fun AppOutlinedTextField(
    state               : TextFieldState,
    modifier            : Modifier                  = Modifier,
    label               : String?                   = null,
    placeholder         : String?                   = null,
    leadingIcon         : ImageVector?              = null,
    trailingIcon        : ImageVector?              = null,
    trailingContent     : @Composable (() -> Unit)? = null,
    supportingText      : String?                   = null,
    isError             : Boolean                   = false,
    errorMessage        : String?                   = null,
    enabled             : Boolean                   = true,
    readOnly            : Boolean                   = false,
    singleLine          : Boolean                   = true,
    maxLength           : Int?                      = null,
    keyboardOptions     : KeyboardOptions           = KeyboardOptions.Default,
    inputTransformation : InputTransformation?      = null,
    style               : AppTextFieldStyle = AppTextFieldStyles.outlined(),
) {
    OutlinedTextField(
        state    = state,
        modifier = modifier,
        enabled  = enabled,
        readOnly = readOnly,
        colors   = style.toOutlinedColors(),
        shape    = style.shape,
        isError  = isError,
        lineLimits = if (singleLine) TextFieldLineLimits.SingleLine
                     else            TextFieldLineLimits.MultiLine(),

        inputTransformation = inputTransformation
            ?: maxLength?.let { InputTransformation.maxLength(it) },

        keyboardOptions = keyboardOptions,

        label = label?.let { { Text(it, style = MaterialTheme.typography.bodyMedium) } },

        placeholder = placeholder?.let {
            { Text(it, style = MaterialTheme.typography.bodyMedium) }
        },

        leadingIcon = leadingIcon?.let {
            { Icon(it, contentDescription = null) }
        },

        trailingIcon = trailingContent ?: trailingIcon?.let {
            { Icon(it, contentDescription = null) }
        },

        supportingText = when {
            isError && errorMessage != null ->
                { { Text(errorMessage, style = MaterialTheme.typography.labelSmall) } }
            supportingText != null ->
                { { Text(supportingText, style = MaterialTheme.typography.labelSmall) } }
            else -> null
        },
    )
}

// ── AppPasswordTextField — Password with eye-toggle ──────────────────────────
/**
 * Password field with an eye icon that toggles text visibility.
 *
 * Uses [SecureTextField] (filled) or [OutlinedSecureTextField] (outlined) — both
 * from M3. These use [TextObfuscationMode] internally instead of VisualTransformation,
 * which is the correct state-based approach.
 *
 * @param state      Password state — create with [rememberTextFieldState].
 * @param label      Field label (default "Password").
 * @param outlined   When true uses [OutlinedSecureTextField] instead of filled.
 * @param isError    Marks the field in error state.
 * @param errorMessage Error hint shown below the field.
 * @param style      Appearance token.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppPasswordTextField(
    state        : TextFieldState   = rememberTextFieldState(),
    modifier     : Modifier         = Modifier,
    label        : String?          = null,
    placeholder  : String?          = null,
    leadingIcon  : ImageVector?     = null,
    outlined     : Boolean          = false,
    isError      : Boolean          = false,
    errorMessage : String?          = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction    = ImeAction.Done,
    ),
    style        : AppTextFieldStyle = AppTextFieldStyles.default(),
) {
    // Toggle visibility state — false = hidden (dots), true = visible
    var showPassword by remember { mutableStateOf(false) }

    val obfuscationMode = if (showPassword) TextObfuscationMode.Visible
                          else              TextObfuscationMode.RevealLastTyped

    val eyeIcon = if (showPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility
    val eyeDesc = if (showPassword) "Hide password"              else "Show password"

    // NOTE: Label lambdas are inlined (not stored in variables) because
    // SecureTextField expects @Composable TextFieldLabelScope.() -> Unit —
    // a scoped receiver type. Storing as plain @Composable () -> Unit loses
    // the scope and causes a type mismatch at compile time.
    if (outlined) {
        OutlinedSecureTextField(
            state               = state,
            modifier            = modifier,
            colors              = style.toOutlinedColors(),
            shape               = style.shape,
            isError             = isError,
            textObfuscationMode = obfuscationMode,
            keyboardOptions     = keyboardOptions,
            label        = if (label != null) { { Text(label, style = MaterialTheme.typography.bodyMedium) } } else null,
            placeholder  = placeholder?.let { { Text(it, style = MaterialTheme.typography.bodyMedium) } },
            leadingIcon = leadingIcon?.let {
                { Icon(it, contentDescription = null) }
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(eyeIcon, contentDescription = eyeDesc)
                }
            },
            supportingText = if (isError && errorMessage != null) {
                { Text(errorMessage, style = MaterialTheme.typography.labelSmall) }
            } else null,
        )
    } else {
        SecureTextField(
            state               = state,
            modifier            = modifier,
            colors              = style.toFilledColors(),
            shape               = style.shape,
            isError             = isError,
            textObfuscationMode = obfuscationMode,
            keyboardOptions     = keyboardOptions,
            label        = if (label != null) { { Text(label, style = MaterialTheme.typography.bodyMedium) } } else null,
            placeholder  = placeholder?.let { { Text(it, style = MaterialTheme.typography.bodyMedium) } },
            leadingIcon = leadingIcon?.let {
                { Icon(it, contentDescription = null) }
            },
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(eyeIcon, contentDescription = eyeDesc)
                }
            },
            supportingText = if (isError && errorMessage != null) {
                { Text(errorMessage, style = MaterialTheme.typography.labelSmall) }
            } else null,
        )
    }
}
