package com.alagamb.petcompose.ui.screens.registration

import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.R
import com.alagamb.petcompose.repo.user.UserRepository
import com.alagamb.petcompose.util.ResultCallBack
import com.alagamb.petcompose.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// ──  MVI CONTRACT  ────────────────────────────────────────────

enum class ValidationStatus {
    IDLE,
    SUCCESS,
    ERROR
}

data class FieldValidationState(
    val status: ValidationStatus = ValidationStatus.IDLE,
    val errorMessage: UiText? = null
)

data class RegistrationUiState(
    val loginEmail: TextFieldState = TextFieldState(),
    val loginPassword: TextFieldState = TextFieldState(),
    val registerUsername: TextFieldState = TextFieldState(),
    val registerEmail: TextFieldState = TextFieldState(),
    val registerPassword: TextFieldState = TextFieldState(),

    val usernameValidation: FieldValidationState = FieldValidationState(),
    val emailValidation: FieldValidationState = FieldValidationState(),
    val passwordValidation: FieldValidationState = FieldValidationState(),

    val loginEmailValidation: FieldValidationState = FieldValidationState(),
    val loginPasswordValidation: FieldValidationState = FieldValidationState(),

    val isLoading: Boolean = false,
    val errorMessage: UiText? = null,
    val successMessage: UiText? = null,
    val isLoginSuccess: Boolean = false,
    val isRegisterSuccess: Boolean = false
)

sealed interface RegistrationIntent {
    // Land Actions
    data object OnLandLoginClick : RegistrationIntent
    data object OnLandSignUpClick : RegistrationIntent

    // Form Submissions
    data object SubmitLogin : RegistrationIntent
    data object SubmitRegister : RegistrationIntent

    // Navigation & Helpers
    data object ClearError : RegistrationIntent
    data object ResetState : RegistrationIntent
}

sealed interface RegistrationSideEffect {
    data object NavigateToLand : RegistrationSideEffect
    data object NavigateToLogin : RegistrationSideEffect
    data object NavigateToRegister : RegistrationSideEffect
    data object NavigateToDashboard : RegistrationSideEffect
    data object NavigateToHome : RegistrationSideEffect
    data class ShowSnackbar(val message: UiText) : RegistrationSideEffect
    data class ShowToast(val message: UiText) : RegistrationSideEffect
}

// ──  VIEW MODEL  ──────────────────────────────────────────────

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var hasAttemptedRegister: Boolean = false
    private var hasAttemptedLogin: Boolean = false

    init {
        Log.i("TATZ", "INIT_REGISTRATION_VIEW_MODEL ")
        observeRegisterInputs()
        observeLoginInputs()
    }

    override fun onCleared() {
        Log.i("TATZ", "CLEAR_REGISTRATION_VIEW_MODEL ")
    }

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<RegistrationSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<RegistrationSideEffect> = _sideEffect.receiveAsFlow()

    fun processIntent(intent: RegistrationIntent) = onIntent(intent)

    private fun onIntent(intent: RegistrationIntent) {
        when (intent) {
            // Land
            is RegistrationIntent.OnLandLoginClick -> {
                sendSideEffect(RegistrationSideEffect.NavigateToLogin)
            }
            is RegistrationIntent.OnLandSignUpClick -> {
                sendSideEffect(RegistrationSideEffect.NavigateToRegister)
            }

            // Form Submissions
            is RegistrationIntent.SubmitLogin -> {
                loginUser()
            }
            is RegistrationIntent.SubmitRegister -> {
                registerUser()
            }

            // Helpers
            is RegistrationIntent.ClearError -> {
                _uiState.update { it.copy(errorMessage = null) }
            }
            is RegistrationIntent.ResetState -> {
                hasAttemptedRegister = false
                hasAttemptedLogin = false
                _uiState.value.loginEmail.clearText()
                _uiState.value.loginPassword.clearText()
                _uiState.value.registerUsername.clearText()
                _uiState.value.registerEmail.clearText()
                _uiState.value.registerPassword.clearText()
                _uiState.value = RegistrationUiState()
            }
        }
    }

    // ──  Reactive Field Validation  ───────────────────────────────

    private fun observeRegisterInputs() {
        viewModelScope.launch {
            snapshotFlow { _uiState.value.registerUsername.text.toString() }
                .collect { text ->
                    _uiState.update { it.copy(usernameValidation = validateUsername(text)) }
                }
        }
        viewModelScope.launch {
            snapshotFlow { _uiState.value.registerEmail.text.toString() }
                .collect { text ->
                    _uiState.update { it.copy(emailValidation = validateEmail(text)) }
                }
        }
        viewModelScope.launch {
            snapshotFlow { _uiState.value.registerPassword.text.toString() }
                .collect { text ->
                    _uiState.update { it.copy(passwordValidation = validatePassword(text)) }
                }
        }
    }

    private fun validateUsername(text: String): FieldValidationState {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            return if (hasAttemptedRegister) {
                FieldValidationState(ValidationStatus.ERROR, UiText.StringResource(R.string.error_username_empty))
            } else {
                FieldValidationState(ValidationStatus.IDLE)
            }
        }
        val parts = trimmed.split("\\s+".toRegex()).filter { it.isNotEmpty() }
        return if (parts.size >= 2) {
            FieldValidationState(ValidationStatus.SUCCESS)
        } else {
            FieldValidationState(ValidationStatus.ERROR, UiText.StringResource(R.string.error_name_first_last))
        }
    }

    private fun validateEmail(text: String): FieldValidationState {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) {
            return if (hasAttemptedRegister) {
                FieldValidationState(ValidationStatus.ERROR, UiText.StringResource(R.string.error_email_empty))
            } else {
                FieldValidationState(ValidationStatus.IDLE)
            }
        }
        return if (android.util.Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()) {
            FieldValidationState(ValidationStatus.SUCCESS)
        } else {
            FieldValidationState(ValidationStatus.ERROR, UiText.StringResource(R.string.error_invalid_email))
        }
    }

    private fun validatePassword(text: String): FieldValidationState {
        if (text.isEmpty()) {
            return if (hasAttemptedRegister) {
                FieldValidationState(ValidationStatus.ERROR, UiText.StringResource(R.string.error_password_empty))
            } else {
                FieldValidationState(ValidationStatus.IDLE)
            }
        }
        return if (text.length > 8) {
            FieldValidationState(ValidationStatus.SUCCESS)
        } else {
            FieldValidationState(ValidationStatus.ERROR, UiText.StringResource(R.string.error_password_more_than_8))
        }
    }

    // ──  UI Validation & Registration  ────────────────────────────

    private fun registerUser() {
        if (_uiState.value.isLoading) return
        hasAttemptedRegister = true

        val currentState = _uiState.value
        val username = currentState.registerUsername.text.toString().trim()
        val email = currentState.registerEmail.text.toString().trim()
        val password = currentState.registerPassword.text.toString()

        val uVal = validateUsername(username)
        val eVal = validateEmail(email)
        val pVal = validatePassword(password)

        _uiState.update {
            it.copy(
                usernameValidation = uVal,
                emailValidation = eVal,
                passwordValidation = pVal
            )
        }

        // Empty field toasts
        if (username.isEmpty()) {
            val error = UiText.StringResource(R.string.error_username_empty)
            _uiState.update { it.copy(errorMessage = error) }
            sendSideEffect(RegistrationSideEffect.ShowToast(error))
            return
        }
        if (email.isEmpty()) {
            val error = UiText.StringResource(R.string.error_email_empty)
            _uiState.update { it.copy(errorMessage = error) }
            sendSideEffect(RegistrationSideEffect.ShowToast(error))
            return
        }
        if (password.isEmpty()) {
            val error = UiText.StringResource(R.string.error_password_empty)
            _uiState.update { it.copy(errorMessage = error) }
            sendSideEffect(RegistrationSideEffect.ShowToast(error))
            return
        }

        // Format / length checks
        if (uVal.status != ValidationStatus.SUCCESS) {
            _uiState.update { it.copy(errorMessage = uVal.errorMessage) }
            return
        }
        if (eVal.status != ValidationStatus.SUCCESS) {
            _uiState.update { it.copy(errorMessage = eVal.errorMessage) }
            return
        }
        if (pVal.status != ValidationStatus.SUCCESS) {
            _uiState.update { it.copy(errorMessage = pVal.errorMessage) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            userRepository.register(username, email, password).collect { result ->
                when (result) {
                    is ResultCallBack.Success -> {
                        val successText = UiText.StringResource(R.string.success_registration)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRegisterSuccess = true,
                                successMessage = successText,
                                errorMessage = null
                            )
                        }
                        sendSideEffect(RegistrationSideEffect.NavigateToDashboard)
                    }
                    is ResultCallBack.Error -> {
                        val errorText = result.messageRes?.let { UiText.StringResource(it) }
                            ?: UiText.DynamicString(result.message ?: "")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = errorText
                            )
                        }
                        sendSideEffect(RegistrationSideEffect.ShowSnackbar(errorText))
                    }
                }
            }
        }
    }

    // ──  Reactive Login Validation  ───────────────────────────────

    private fun observeLoginInputs() {
        viewModelScope.launch {
            snapshotFlow { _uiState.value.loginEmail.text.toString() }
                .collect { text ->
                    _uiState.update { it.copy(loginEmailValidation = validateLoginField(text, R.string.error_email_empty)) }
                }
        }
        viewModelScope.launch {
            snapshotFlow { _uiState.value.loginPassword.text.toString() }
                .collect { text ->
                    _uiState.update { it.copy(loginPasswordValidation = validateLoginField(text, R.string.error_password_empty)) }
                }
        }
    }

    private fun validateLoginField(text: String, emptyErrorRes: Int): FieldValidationState {
        val trimmed = text.trim()
        return if (trimmed.isEmpty()) {
            if (hasAttemptedLogin) {
                FieldValidationState(ValidationStatus.ERROR, UiText.StringResource(emptyErrorRes))
            } else {
                FieldValidationState(ValidationStatus.IDLE)
            }
        } else {
            FieldValidationState(ValidationStatus.SUCCESS)
        }
    }

    // ──  UI Validation & Login  ───────────────────────────────────

    private fun loginUser() {
        if (_uiState.value.isLoading) return
        hasAttemptedLogin = true

        val currentState = _uiState.value
        val email = currentState.loginEmail.text.toString().trim()
        val password = currentState.loginPassword.text.toString()

        val eVal = validateLoginField(email, R.string.error_email_empty)
        val pVal = validateLoginField(password, R.string.error_password_empty)

        _uiState.update {
            it.copy(
                loginEmailValidation = eVal,
                loginPasswordValidation = pVal
            )
        }

        // Empty field toasts
        if (email.isEmpty()) {
            val error = UiText.StringResource(R.string.error_email_empty)
            _uiState.update { it.copy(errorMessage = error) }
            sendSideEffect(RegistrationSideEffect.ShowToast(error))
            return
        }
        if (password.isEmpty()) {
            val error = UiText.StringResource(R.string.error_password_empty)
            _uiState.update { it.copy(errorMessage = error) }
            sendSideEffect(RegistrationSideEffect.ShowToast(error))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            userRepository.login(email, password).collect { result ->
                when (result) {
                    is ResultCallBack.Success -> {
                        val successText = UiText.StringResource(R.string.success_login)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isLoginSuccess = true,
                                successMessage = successText,
                                errorMessage = null
                            )
                        }
                        sendSideEffect(RegistrationSideEffect.NavigateToDashboard)
                    }
                    is ResultCallBack.Error -> {
                        val errorText = result.messageRes?.let { UiText.StringResource(it) }
                            ?: UiText.DynamicString(result.message ?: "")
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = errorText
                            )
                        }
                        sendSideEffect(RegistrationSideEffect.ShowSnackbar(errorText))
                    }
                }
            }
        }
    }

    private fun sendSideEffect(effect: RegistrationSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}