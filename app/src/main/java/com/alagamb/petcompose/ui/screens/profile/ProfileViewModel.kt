package com.alagamb.petcompose.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.data.preferences.AppPreferencesManager
import com.alagamb.petcompose.data.preferences.ThemeMode
import com.alagamb.petcompose.repo.pet.PetRepository
import com.alagamb.petcompose.repo.request.RequestRepository
import com.alagamb.petcompose.repo.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val uiState: StateFlow<ProfileUiState> = combine(
        userRepository.currentUser,
        requestRepository.getAllRequests(),
        appPreferencesManager.themeModeFlow,
        appPreferencesManager.appLanguageFlow
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
        initialValue = ProfileUiState(selectedLanguage = appPreferencesManager.getInitialLanguage())
    )

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            appPreferencesManager.setThemeMode(mode)
        }
    }

    fun setLanguage(languageCode: String) {
        viewModelScope.launch {
            appPreferencesManager.setAppLanguage(languageCode)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}
