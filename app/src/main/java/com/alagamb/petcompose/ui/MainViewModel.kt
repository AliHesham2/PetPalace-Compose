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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userRepository: UserRepository,
    appPreferencesManager: AppPreferencesManager
) : ViewModel() {

    val themeMode: StateFlow<ThemeMode> = appPreferencesManager.themeModeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeMode.SYSTEM
        )

    val startDestination: StateFlow<String?> = userRepository.currentUser
        .map { user ->
            if (user != null) AppRoute.MAIN_ROUTE else AppRoute.AUTH_GRAPH
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
}
