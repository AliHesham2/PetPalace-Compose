package com.alagamb.petcompose.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.repo.user.UserRepository
import com.alagamb.petcompose.ui.nav.AppRoute
import com.alagamb.petcompose.data.preferences.AppPreferencesManager
import com.alagamb.petcompose.data.preferences.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val startDestination: String,
        val themeMode: ThemeMode,
        val language: String
    ) : MainActivityUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository,
    private val appPreferencesManager: AppPreferencesManager
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> = combine(
        userRepository.currentUser,
        appPreferencesManager.themeModeFlow,
        appPreferencesManager.appLanguageFlow
    ) { user, themeMode, language ->
        val destination = if (user != null) AppRoute.MAIN_ROUTE else AppRoute.AUTH_GRAPH
        MainActivityUiState.Success(
            startDestination = destination,
            themeMode = themeMode,
            language = language
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MainActivityUiState.Loading
    )

    fun toggleLanguage() {
        viewModelScope.launch {
            val currentLanguage = when (val state = uiState.value) {
                is MainActivityUiState.Success -> state.language
                is MainActivityUiState.Loading -> appPreferencesManager.getInitialLanguage()
            }
            val nextLanguage = if (currentLanguage.startsWith("ar", ignoreCase = true)) "en" else "ar"
            setAppLanguage(nextLanguage)
        }
    }

    fun setAppLanguage(languageCode: String) {
        viewModelScope.launch {
            appPreferencesManager.setAppLanguage(languageCode)
        }
    }
}
