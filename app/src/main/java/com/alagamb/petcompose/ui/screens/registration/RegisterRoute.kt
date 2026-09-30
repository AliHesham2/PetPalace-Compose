package com.alagamb.petcompose.ui.screens.registration

import com.alagamb.petcompose.ui.commonui.brand.AppLogo

import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alagamb.petcompose.R
import com.alagamb.petcompose.ui.commonui.buttons.AppButtonStyles
import com.alagamb.petcompose.ui.commonui.buttons.AppMorphingButton
import com.alagamb.petcompose.ui.commonui.customize.AppModifier
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.textfield.AppOutlinedTextField
import com.alagamb.petcompose.ui.commonui.textfield.AppPasswordTextField
import com.alagamb.petcompose.ui.commonui.textfield.AppTextFieldStyle
import com.alagamb.petcompose.ui.commonui.textfield.AppTextFieldStyles
import com.alagamb.petcompose.ui.theme.PetComposeTheme
import com.alagamb.petcompose.util.LocalSnackbarHostState

@Composable
fun RegisterRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = onBackClick,
    onNavigateToDashboard: () -> Unit = {},
    viewModel: RegistrationViewModel? = null,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val state = viewModel?.uiState?.collectAsStateWithLifecycle()?.value ?: RegistrationUiState()
    val snackBarHostState = LocalSnackbarHostState.current
    val context = LocalContext.current

    LaunchedEffect(viewModel) {
        viewModel?.sideEffect?.collect { effect ->
            when (effect) {
                is RegistrationSideEffect.NavigateToDashboard -> onNavigateToDashboard()
                is RegistrationSideEffect.NavigateToLogin -> onNavigateToLogin()
                is RegistrationSideEffect.ShowSnackbar -> {
                    snackBarHostState.showSnackbar(effect.message.asString(context))
                }
                is RegistrationSideEffect.ShowToast -> {
                    Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    RegisterScreen(
        state = state,
        onBackClick = onBackClick,
        onRegisterClick = { viewModel?.processIntent(RegistrationIntent.SubmitRegister) },
        onNavigateToLogin = onNavigateToLogin,
        modifier = modifier,
        windowAdaptiveInfo = windowAdaptiveInfo
    )
}

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    state: RegistrationUiState,
    onBackClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit = {},
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val usernameStyle = when (state.usernameValidation.status) {
        ValidationStatus.SUCCESS -> AppTextFieldStyles.success()
        ValidationStatus.ERROR -> AppTextFieldStyles.danger()
        ValidationStatus.IDLE -> AppTextFieldStyles.outlined()
    }
    val usernameIsError = state.usernameValidation.status == ValidationStatus.ERROR
    val usernameErrorMsg = state.usernameValidation.errorMessage?.asString()

    val emailStyle = when (state.emailValidation.status) {
        ValidationStatus.SUCCESS -> AppTextFieldStyles.success()
        ValidationStatus.ERROR -> AppTextFieldStyles.danger()
        ValidationStatus.IDLE -> AppTextFieldStyles.outlined()
    }
    val emailIsError = state.emailValidation.status == ValidationStatus.ERROR
    val emailErrorMsg = state.emailValidation.errorMessage?.asString()

    val passwordStyle = when (state.passwordValidation.status) {
        ValidationStatus.SUCCESS -> AppTextFieldStyles.success()
        ValidationStatus.ERROR -> AppTextFieldStyles.danger()
        ValidationStatus.IDLE -> AppTextFieldStyles.outlined()
    }
    val passwordIsError = state.passwordValidation.status == ValidationStatus.ERROR
    val passwordErrorMsg = state.passwordValidation.errorMessage?.asString()

    val isTablet = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val isWideScreen = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )
    val maxCardWidth = RegisterModifiers.calculateCardWidth(isTablet, isWideScreen)
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier.then(RegisterModifiers.root(MaterialTheme.colorScheme.background)),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = RegisterModifiers.contentScroll(maxCardWidth, scrollState)
        ) {
            // ── Top Navigation Bar ───────────────────────────────────
            Row(
                modifier = RegisterModifiers.topNavRow,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    modifier = RegisterModifiers.backButtonSurface
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Hero Section ─────────────────────────────────────────
            Column(
                modifier = RegisterModifiers.heroColumn,
                horizontalAlignment = if (isWideScreen) Alignment.CenterHorizontally else Alignment.Start
            ) {
                AppLogo(
                    size = if (isWideScreen) 72.dp else 64.dp,
                    elevation = 6.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.register_welcome),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = if (isWideScreen) TextAlign.Center else TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.register_welcome_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = if (isWideScreen) TextAlign.Center else TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Input Fields ─────────────────────────────────────────
            RegisterInputField(
                state = state.registerUsername,
                isPassword = false,
                icon = Icons.Default.Person,
                label = stringResource(R.string.register_name),
                hint = stringResource(R.string.register_name_hint),
                isError = usernameIsError,
                errorMessage = usernameErrorMsg,
                style = usernameStyle
            )

            Spacer(modifier = Modifier.height(16.dp))

            RegisterInputField(
                state = state.registerEmail,
                isPassword = false,
                icon = Icons.Default.Email,
                label = stringResource(R.string.auth_email),
                hint = stringResource(R.string.auth_email_hint),
                isError = emailIsError,
                errorMessage = emailErrorMsg,
                style = emailStyle
            )

            Spacer(modifier = Modifier.height(16.dp))

            RegisterInputField(
                state = state.registerPassword,
                isPassword = true,
                icon = Icons.Default.Lock,
                label = stringResource(R.string.auth_password),
                hint = stringResource(R.string.auth_password_hint),
                isError = passwordIsError,
                errorMessage = passwordErrorMsg,
                style = passwordStyle
            )

            // Password length requirement note
            Text(
                text = stringResource(R.string.auth_password_requirement),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = RegisterModifiers.passwordRequirementNote
            )

            // General Error Banner if present
            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = RegisterModifiers.errorBanner,
                    shape = AppShape.Medium,
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        modifier = RegisterModifiers.errorBannerRow,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = state.errorMessage.asString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Submit Button ────────────────────────────────────────
            AppMorphingButton(
                text = stringResource(R.string.register_button),
                isLoading = state.isLoading,
                onClick = onRegisterClick,
                style = AppButtonStyles.primary(),
                height = 52.dp,
                modifier = RegisterModifiers.submitButton
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Footer: Already have an account? Log In ──────────────
            Row(
                modifier = RegisterModifiers.footerRow,
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.auth_already_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.auth_action_login),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
        }
    }
}

@Composable
private fun RegisterInputField(
    state: TextFieldState,
    isPassword: Boolean,
    icon: ImageVector,
    label: String,
    hint: String,
    isError: Boolean,
    errorMessage: String?,
    style: AppTextFieldStyle,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        if (!isPassword) {
            AppOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                leadingIcon = icon,
                placeholder = hint,
                isError = isError,
                errorMessage = errorMessage,
                style = style,
            )
        } else {
            AppPasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                leadingIcon = icon,
                label = hint,
                outlined = true,
                isError = isError,
                errorMessage = errorMessage,
                style = style,
            )
        }
    }
}

// ──  PREVIEWS  ───────────────────────────────────────────────

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun RegisterScreenPreview() {
    PetComposeTheme {
        RegisterScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun RegisterScreenDarkPreview() {
    PetComposeTheme(darkTheme = true) {
        RegisterScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(
    name = "Foldable",
    device = "spec:width=673dp,height=841dp,dpi=420",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenFoldablePreview() {
    PetComposeTheme {
        RegisterScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240",
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RegisterScreenTabletPreview() {
    PetComposeTheme {
        RegisterScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onRegisterClick = {}
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// RegisterModifiers — Centralized layout and styling tokens from AppModifier
// ─────────────────────────────────────────────────────────────────────────────
private typealias RegisterModifiers = AppModifier.Registration