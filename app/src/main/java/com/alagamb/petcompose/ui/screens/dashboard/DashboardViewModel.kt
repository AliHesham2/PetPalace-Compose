package com.alagamb.petcompose.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.data.model.dashboard.AnnouncementItem
import com.alagamb.petcompose.data.model.dashboard.FeaturedPetItem
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem
import com.alagamb.petcompose.data.model.user.User
import com.alagamb.petcompose.repo.pet.PetRepository
import com.alagamb.petcompose.repo.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class MainTab {
    DASHBOARD,
    PROFILE
}

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val petRepository: PetRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = userRepository.currentUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val announcements: StateFlow<List<AnnouncementItem>> = petRepository.getAnnouncements()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<PetCategoryItem>> = petRepository.getCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val featuredPets: StateFlow<List<FeaturedPetItem>> = petRepository.getFeaturedPets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedTab = MutableStateFlow(MainTab.DASHBOARD)
    val selectedTab: StateFlow<MainTab> = _selectedTab.asStateFlow()

    init {
        viewModelScope.launch {
            petRepository.ensureSeeded()
        }
    }

    fun selectTab(tab: MainTab) {
        _selectedTab.update { tab }
    }

    fun toggleFavorite(petId: String, currentFavorite: Boolean) {
        viewModelScope.launch {
            petRepository.toggleFavorite(petId, !currentFavorite)
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            userRepository.logout()
            onLoggedOut()
        }
    }
}
