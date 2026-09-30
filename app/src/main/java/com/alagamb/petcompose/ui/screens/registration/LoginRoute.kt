package com.alagamb.petcompose.ui.screens.registration

import com.alagamb.petcompose.ui.commonui.brand.AppLogo

import android.widget.Toast
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pets
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.alagamb.petcompose.ui.commonui.customize.AppColors
import com.alagamb.petcompose.ui.commonui.customize.AppShape
import com.alagamb.petcompose.ui.commonui.textfield.AppOutlinedTextField
import com.alagamb.petcompose.ui.commonui.textfield.AppPasswordTextField
import com.alagamb.petcompose.ui.commonui.textfield.AppTextFieldStyle
import com.alagamb.petcompose.ui.commonui.textfield.AppTextFieldStyles
import com.alagamb.petcompose.ui.theme.PetComposeTheme
import com.alagamb.petcompose.util.LocalSnackbarHostState

@Composable
fun LoginRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToRegister: () -> Unit = {},
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
                is RegistrationSideEffect.NavigateToRegister -> onNavigateToRegister()
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

    LoginScreen(
        state = state,
        onBackClick = onBackClick,
        onLoginClick = { viewModel?.processIntent(RegistrationIntent.SubmitLogin) },
        onNavigateToRegister = onNavigateToRegister,
        modifier = modifier,
        windowAdaptiveInfo = windowAdaptiveInfo
    )
}

@Composable
fun LoginScreen(
    state: RegistrationUiState,
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit = {},
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo()
) {
    val emailStyle = when (state.loginEmailValidation.status) {
        ValidationStatus.SUCCESS -> AppTextFieldStyles.success()
        ValidationStatus.ERROR -> AppTextFieldStyles.danger()
        ValidationStatus.IDLE -> AppTextFieldStyles.outlined()
    }
    val emailIsError = state.loginEmailValidation.status == ValidationStatus.ERROR
    val emailErrorMsg = state.loginEmailValidation.errorMessage?.asString()

    val passwordStyle = when (state.loginPasswordValidation.status) {
        ValidationStatus.SUCCESS -> AppTextFieldStyles.success()
        ValidationStatus.ERROR -> AppTextFieldStyles.danger()
        ValidationStatus.IDLE -> AppTextFieldStyles.outlined()
    }
    val passwordIsError = state.loginPasswordValidation.status == ValidationStatus.ERROR
    val passwordErrorMsg = state.loginPasswordValidation.errorMessage?.asString()

    val isTablet = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )
    val isWideScreen = windowAdaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
    )

    val maxCardWidth = when {
        isTablet -> 440.dp
        isWideScreen -> 460.dp
        else -> 500.dp
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = maxCardWidth)
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ── Top Navigation Bar ───────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(42.dp)
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = if (isWideScreen) Alignment.CenterHorizontally else Alignment.Start
            ) {
                AppLogo(
                    size = if (isWideScreen) 72.dp else 64.dp,
                    elevation = 6.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.login_welcome),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = if (isWideScreen) TextAlign.Center else TextAlign.Start
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.login_welcome_body),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = if (isWideScreen) TextAlign.Center else TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ── Input Fields ─────────────────────────────────────────
            LoginInputField(
                state = state.loginEmail,
                isPassword = false,
                icon = Icons.Default.Email,
                label = stringResource(R.string.auth_email),
                hint = stringResource(R.string.auth_email_hint),
                isError = emailIsError,
                errorMessage = emailErrorMsg,
                style = emailStyle
            )

            Spacer(modifier = Modifier.height(16.dp))

            LoginInputField(
                state = state.loginPassword,
                isPassword = true,
                icon = Icons.Default.Lock,
                label = stringResource(R.string.auth_password),
                hint = stringResource(R.string.auth_password_hint),
                isError = passwordIsError,
                errorMessage = passwordErrorMsg,
                style = passwordStyle
            )

            // Forgot Password Link
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = stringResource(R.string.auth_forgot_password),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { /* future forgot-password flow */ }
                )
            }

            // General Error Banner if present
            if (state.errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = AppShape.Medium,
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
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
                text = stringResource(R.string.login_Button),
                isLoading = state.isLoading,
                onClick = onLoginClick,
                style = AppButtonStyles.primary(),
                height = 52.dp,
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Footer: Don't have an account? Sign Up ────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.auth_dont_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.auth_action_sign_up),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}

@Composable
private fun LoginInputField(
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
fun LoginScreenPreview() {
    PetComposeTheme {
        LoginScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onLoginClick = {}
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
fun LoginScreenDarkPreview() {
    PetComposeTheme(darkTheme = true) {
        LoginScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onLoginClick = {}
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
fun LoginScreenFoldablePreview() {
    PetComposeTheme {
        LoginScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onLoginClick = {}
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
fun LoginScreenTabletPreview() {
    PetComposeTheme {
        LoginScreen(
            state = RegistrationUiState(),
            onBackClick = {},
            onLoginClick = {}
        )
    }
}