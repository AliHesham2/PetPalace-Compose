package com.alagamb.petcompose.ui.screens.products.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.data.model.product.ProductItem
import com.alagamb.petcompose.repo.pet.PetRepository
import com.alagamb.petcompose.repo.request.RequestRepository
import com.alagamb.petcompose.ui.nav.AppArgs
import com.alagamb.petcompose.ui.screens.products.productlist.ProductListMockData
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
import javax.inject.Inject

// ──  MVI CONTRACT  ────────────────────────────────────────────

data class ProductDetailsUiState(
    val product: ProductItem? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val isSendingRequest: Boolean = false,
    val isRequestSent: Boolean = false,
    val errorMessage: UiText? = null
) {
    /** Computes total price formatted string if possible, or returns unit price */
    val totalPrice: String
        get() {
            val item = product ?: return "$0.00"
            val priceNumber = item.price.replace("$", "").trim().toDoubleOrNull()
            return if (priceNumber != null) {
                String.format("$%.2f", priceNumber * quantity)
            } else {
                item.price
            }
        }
}

sealed interface ProductDetailsIntent {
    data class LoadProduct(val productId: String) : ProductDetailsIntent
    data object OnToggleFavorite : ProductDetailsIntent
    data object IncreaseQuantity : ProductDetailsIntent
    data object DecreaseQuantity : ProductDetailsIntent
    data object SendRequest : ProductDetailsIntent
}

sealed interface ProductDetailsSideEffect {
    data class ShowToast(val message: UiText) : ProductDetailsSideEffect
    data object NavigateBack : ProductDetailsSideEffect
}

// ──  VIEW MODEL  ──────────────────────────────────────────────

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val petRepository: PetRepository,
    private val requestRepository: RequestRepository
) : ViewModel() {

    private var activeProductId: String = savedStateHandle.get<String>(AppArgs.PRODUCT_ID).orEmpty()

    private val _uiState = MutableStateFlow(
        ProductDetailsUiState(
            product = ProductListMockData.allProducts.find { it.id == activeProductId },
            isFavorite = ProductListMockData.allProducts.find { it.id == activeProductId }?.isFavorite ?: false,
            isLoading = activeProductId.isNotEmpty()
        )
    )
    val uiState: StateFlow<ProductDetailsUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ProductDetailsSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ProductDetailsSideEffect> = _sideEffect.receiveAsFlow()

    init {
        if (activeProductId.isNotEmpty()) {
            loadPetDetails(activeProductId)
        }
    }

    private fun loadPetDetails(id: String) {
        activeProductId = id
        val mockFallback = ProductListMockData.allProducts.find { it.id == id }
        _uiState.update { current ->
            current.copy(
                product = mockFallback ?: current.product,
                isFavorite = mockFallback?.isFavorite ?: current.isFavorite,
                isLoading = true,
                quantity = 1,
                isRequestSent = false
            )
        }
        viewModelScope.launch {
            petRepository.getPetDetails(id).collect { result ->
                when (result) {
                    is ResultCallBack.Success -> {
                        _uiState.update { current ->
                            current.copy(
                                product = result.data,
                                isFavorite = result.data.isFavorite,
                                isLoading = false
                            )
                        }
                    }
                    is ResultCallBack.Error -> {
                        _uiState.update { current ->
                            current.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    fun processIntent(intent: ProductDetailsIntent) = onIntent(intent)

    private fun onIntent(intent: ProductDetailsIntent) {
        when (intent) {
            is ProductDetailsIntent.LoadProduct -> {
                loadPetDetails(intent.productId)
            }
            is ProductDetailsIntent.OnToggleFavorite -> {
                val petId = _uiState.value.product?.id ?: activeProductId
                val currentFav = _uiState.value.isFavorite
                val newFav = !currentFav
                _uiState.update { current ->
                    current.copy(
                        isFavorite = newFav,
                        product = current.product?.copy(isFavorite = newFav)
                    )
                }
                viewModelScope.launch {
                    petRepository.toggleFavorite(petId, newFav)
                }
            }
            is ProductDetailsIntent.IncreaseQuantity -> {
                _uiState.update { current ->
                    if (current.quantity < 99) {
                        current.copy(quantity = current.quantity + 1)
                    } else {
                        current
                    }
                }
            }
            is ProductDetailsIntent.DecreaseQuantity -> {
                _uiState.update { current ->
                    if (current.quantity > 1) {
                        current.copy(quantity = current.quantity - 1)
                    } else {
                        current
                    }
                }
            }
            is ProductDetailsIntent.SendRequest -> {
                val currentProduct = _uiState.value.product ?: return
                viewModelScope.launch {
                    _uiState.update { it.copy(isSendingRequest = true) }
                    when (val result = requestRepository.sendRequest(currentProduct, _uiState.value.quantity)) {
                        is ResultCallBack.Success -> {
                            _uiState.update { it.copy(isSendingRequest = false, isRequestSent = true) }
                            sendSideEffect(
                                ProductDetailsSideEffect.ShowToast(
                                    UiText.StringResource(
                                        com.alagamb.petcompose.R.string.request_sent_success,
                                        currentProduct.name
                                    )
                                )
                            )
                        }
                        is ResultCallBack.Error -> {
                            _uiState.update { it.copy(isSendingRequest = false) }
                            sendSideEffect(
                                ProductDetailsSideEffect.ShowToast(
                                    result.message?.let { UiText.DynamicString(it) }
                                        ?: UiText.StringResource(com.alagamb.petcompose.R.string.request_failed)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun sendSideEffect(effect: ProductDetailsSideEffect) {
        viewModelScope.launch {
            _sideEffect.send(effect)
        }
    }
}
