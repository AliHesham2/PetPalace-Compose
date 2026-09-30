package com.alagamb.petcompose.ui.screens.profile

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.data.preferences.AppPreferencesManager
import com.alagamb.petcompose.data.preferences.ThemeMode
import com.alagamb.petcompose.repo.pet.PetRepository
import com.alagamb.petcompose.repo.request.RequestRepository
import com.alagamb.petcompose.repo.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val username: String = "",
    val email: String = "",
    val requestsCount: Int = 0,
    val favoritesCount: Int = 0,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val selectedLanguage: String = "en"
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val appPreferencesManager: AppPreferencesManager,
    private val requestRepository: RequestRepository,
    private val petRepository: PetRepository
) : ViewModel() {

    private val currentLocaleTag = AppCompatDelegate.getApplicationLocales()[0]?.language ?: "en"
    private val _selectedLanguage = MutableStateFlow(currentLocaleTag)

    val uiState: StateFlow<ProfileUiState> = combine(
        userRepository.currentUser,
        requestRepository.getAllRequests(),
        appPreferencesManager.themeModeFlow,
        _selectedLanguage
    ) { user, requests, themeMode, language ->
        ProfileUiState(
            username = user?.username.orEmpty().ifBlank { "Aly Mohamed" },
            email = user?.email.orEmpty().ifBlank { "aly@petcompose.app" },
            requestsCount = requests.size,
            favoritesCount = 4,
            themeMode = themeMode,
            selectedLanguage = language
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState(selectedLanguage = currentLocaleTag)
    )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            appPreferencesManager.setThemeMode(mode)
            val nightMode = when (mode) {
                ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            }
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }

    fun setLanguage(languageCode: String) {
        val appLocales = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(appLocales)
        _selectedLanguage.value = languageCode
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
