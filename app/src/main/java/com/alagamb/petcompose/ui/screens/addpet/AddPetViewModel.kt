package com.alagamb.petcompose.ui.screens.addpet

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.db.table.pet.PetTable
import com.alagamb.petcompose.data.model.dashboard.PetCategoryItem
import com.alagamb.petcompose.repo.pet.PetRepository
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
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

// ──  MVI CONTRACT  ────────────────────────────────────────────

data class AddPetUiState(
    val nameState: TextFieldState = TextFieldState(),
    val breedState: TextFieldState = TextFieldState(),
    val ageState: TextFieldState = TextFieldState(),
    val priceState: TextFieldState = TextFieldState(),
    val distanceState: TextFieldState = TextFieldState(),
    val descriptionState: TextFieldState = TextFieldState(),

    val categories: List<PetCategoryItem> = emptyList(),
    val selectedCategory: PetCategoryItem? = null,
    val selectedGender: String = "Male",
    val selectedTag: String = "Best Match",
    val isFeatured: Boolean = true,

    // Field-level error messages
    val nameError: UiText? = null,
    val breedError: UiText? = null,
    val ageError: UiText? = null,
    val priceError: UiText? = null,
    val descriptionError: UiText? = null,

    val isLoading: Boolean = false,
    val isSuccess: Boolean = false
)

sealed interface AddPetIntent {
    data class SelectCategory(val category: PetCategoryItem) : AddPetIntent
    data class SelectGender(val gender: String) : AddPetIntent
    data class SelectTag(val tag: String) : AddPetIntent
    data class ToggleFeatured(val isFeatured: Boolean) : AddPetIntent
    data object SubmitPet : AddPetIntent
    data object ClearErrors : AddPetIntent
}

sealed interface AddPetSideEffect {
    data class ShowToast(val message: UiText) : AddPetSideEffect
    data class ShowSnackbar(val message: UiText) : AddPetSideEffect
    data object NavigateBack : AddPetSideEffect
}

// ──  VIEW MODEL  ──────────────────────────────────────────────

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPetUiState())
    val uiState: StateFlow<AddPetUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<AddPetSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<AddPetSideEffect> = _sideEffect.receiveAsFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            petRepository.getCategories().collect { categoryList ->
                _uiState.update { current ->
                    current.copy(
                        categories = categoryList,
                        selectedCategory = current.selectedCategory ?: categoryList.firstOrNull()
                    )
                }
            }
        }
    }

    fun processIntent(intent: AddPetIntent) {
        when (intent) {
            is AddPetIntent.SelectCategory -> {
                _uiState.update { it.copy(selectedCategory = intent.category) }
            }
            is AddPetIntent.SelectGender -> {
                _uiState.update { it.copy(selectedGender = intent.gender) }
            }
            is AddPetIntent.SelectTag -> {
                _uiState.update { it.copy(selectedTag = intent.tag) }
            }
            is AddPetIntent.ToggleFeatured -> {
                _uiState.update { it.copy(isFeatured = intent.isFeatured) }
            }
            is AddPetIntent.ClearErrors -> {
                _uiState.update {
                    it.copy(
                        nameError = null,
                        breedError = null,
                        ageError = null,
                        priceError = null,
                        descriptionError = null
                    )
                }
            }
            is AddPetIntent.SubmitPet -> {
                validateAndSubmit()
            }
        }
    }

    private fun validateAndSubmit() {
        val currentState = _uiState.value
        val name = currentState.nameState.text.toString().trim()
        val breed = currentState.breedState.text.toString().trim()
        val age = currentState.ageState.text.toString().trim()
        val priceInput = currentState.priceState.text.toString().trim()
        val distanceInput = currentState.distanceState.text.toString().trim()
        val description = currentState.descriptionState.text.toString().trim()

        var hasError = false
        var nameErr: UiText? = null
        var breedErr: UiText? = null
        var ageErr: UiText? = null
        var priceErr: UiText? = null
        var descErr: UiText? = null

        if (name.isBlank()) {
            nameErr = UiText.StringResource(R.string.add_pet_error_name)
            hasError = true
        }

        if (breed.isBlank()) {
            breedErr = UiText.StringResource(R.string.add_pet_error_breed)
            hasError = true
        }

        if (age.isBlank()) {
            ageErr = UiText.StringResource(R.string.add_pet_error_age)
            hasError = true
        }

        val parsedPrice = priceInput.replace("$", "").trim()
        if (parsedPrice.isBlank() || parsedPrice.toDoubleOrNull() == null) {
            priceErr = UiText.StringResource(R.string.add_pet_error_price)
            hasError = true
        }

        if (description.length < 10) {
            descErr = UiText.StringResource(R.string.add_pet_error_desc)
            hasError = true
        }

        if (hasError) {
            _uiState.update {
                it.copy(
                    nameError = nameErr,
                    breedError = breedErr,
                    ageError = ageErr,
                    priceError = priceErr,
                    descriptionError = descErr
                )
            }
            return
        }

        // Clean slate errors
        _uiState.update {
            it.copy(
                nameError = null,
                breedError = null,
                ageError = null,
                priceError = null,
                descriptionError = null,
                isLoading = true
            )
        }

        val category = currentState.selectedCategory
        val categoryId = category?.id ?: "others"
        val categoryName = category?.name ?: "Others"

        // Pick harmonious aesthetic gradients based on category
        val (startColor, endColor) = when (categoryId.lowercase()) {
            "dogs" -> Pair(0xFFFFE0B2, 0xFFFFCC80)
            "cats" -> Pair(0xFFFFCDD2, 0xFFEF9A9A)
            "birds" -> Pair(0xFFC8E6C9, 0xFFA5D6A7)
            "fish" -> Pair(0xFFB3E5FC, 0xFF81D4FA)
            else -> Pair(0xFFE1BEE7, 0xFFCE93D8)
        }

        val formattedPrice = if (parsedPrice.startsWith("$")) parsedPrice else "$$parsedPrice"
        val formattedDistance = if (distanceInput.isNotBlank()) distanceInput else "1.5 km"

        val petId = "pet_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"

        val petTable = PetTable(
            id = petId,
            name = name,
            categoryId = categoryId,
            categoryName = categoryName,
            breed = breed,
            age = age,
            gender = currentState.selectedGender,
            distance = formattedDistance,
            description = description,
            price = formattedPrice,
            rating = 5.0f,
            reviewCount = 1,
            isFavorite = false,
            isFeatured = currentState.isFeatured,
            tag = currentState.selectedTag,
            startColor = startColor,
            endColor = endColor
        )

        viewModelScope.launch {
            val result = petRepository.addPet(petTable)
            _uiState.update { it.copy(isLoading = false) }

            when (result) {
                is ResultCallBack.Success -> {
                    _uiState.update { it.copy(isSuccess = true) }
                    _sideEffect.send(AddPetSideEffect.ShowToast(UiText.StringResource(R.string.add_pet_success)))
                    _sideEffect.send(AddPetSideEffect.NavigateBack)
                }
                is ResultCallBack.Error -> {
                    val resId = result.messageRes
                    val errorMsg = result.message
                    val msg = when {
                        resId != null -> UiText.StringResource(resId)
                        !errorMsg.isNullOrBlank() -> UiText.DynamicString(errorMsg)
                        else -> UiText.StringResource(R.string.error_unknown)
                    }
                    _sideEffect.send(AddPetSideEffect.ShowSnackbar(msg))
                }
            }
        }
    }
}
