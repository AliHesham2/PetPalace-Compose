package com.alagamb.petcompose.ui.screens.products.productlist

import android.net.Uri
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.alagamb.petcompose.data.model.product.ProductItem
import com.alagamb.petcompose.repo.pet.PetRepository
import com.alagamb.petcompose.ui.nav.AppArgs
import com.alagamb.petcompose.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

// ──  MVI CONTRACT  ────────────────────────────────────────────

data class ProductListUiState(
    val title: String = "",
    val searchState: TextFieldState = TextFieldState(),
    val filterTags: List<String> = listOf("All"),
    val selectedTag: String? = null
)

sealed interface ProductListIntent {
    data class OnSelectTag(val tag: String?) : ProductListIntent
    data class OnToggleFavorite(val productId: String, val currentFavorite: Boolean) : ProductListIntent
    data object ClearSearch : ProductListIntent
}

sealed interface ProductListSideEffect {
    data class ShowToast(val message: UiText) : ProductListSideEffect
    data class NavigateToProductDetails(val productId: String) : ProductListSideEffect
}

// ──  VIEW MODEL  ──────────────────────────────────────────────

@HiltViewModel
class ProductListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val petRepository: PetRepository
) : ViewModel() {

    private data class SearchFilterParams(
        val query: String = "",
        val tag: String? = null
    )

    private val categoryTitle: String = savedStateHandle.get<String>(AppArgs.CATEGORY_TITLE)
        ?.let { Uri.decode(it) }
        .orEmpty()

    val searchState = TextFieldState()
    private val _selectedTag = MutableStateFlow<String?>(null)

    // State IS a formula over source flows:
    val uiState: StateFlow<ProductListUiState> = combine(
        petRepository.getFilterTags(categoryTitle),
        _selectedTag
    ) { tags, selectedTag ->
        ProductListUiState(
            title = categoryTitle,
            searchState = searchState,
            filterTags = tags,
            selectedTag = selectedTag
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProductListUiState(title = categoryTitle, searchState = searchState)
    )

    private val _sideEffect = Channel<ProductListSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ProductListSideEffect> = _sideEffect.receiveAsFlow()

    // Reactive Paging 3 stream derived from search flow and selected tag flow:
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val petsPagingFlow: Flow<PagingData<ProductItem>> = combine(
        snapshotFlow { searchState.text.toString() }
            .debounce(300.milliseconds),
        _selectedTag
    ) { query, tag ->
        SearchFilterParams(query = query.trim(), tag = tag)
    }
        .distinctUntilChanged()
        .flatMapLatest { params ->
            petRepository.getPetsPaged(
                category = categoryTitle,
                tag = params.tag,
                searchQuery = params.query
            )
        }
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            petRepository.ensureSeeded()
        }
    }

    fun processIntent(intent: ProductListIntent) = onIntent(intent)

    private fun onIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.OnSelectTag -> {
                _selectedTag.value = if (intent.tag == "All" || intent.tag == _selectedTag.value) null else intent.tag
            }
            is ProductListIntent.OnToggleFavorite -> {
                viewModelScope.launch {
                    petRepository.toggleFavorite(intent.productId, !intent.currentFavorite)
                }
            }
            is ProductListIntent.ClearSearch -> {
                searchState.clearText()
            }
        }
    }

    fun sendSideEffect(effect: ProductListSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
