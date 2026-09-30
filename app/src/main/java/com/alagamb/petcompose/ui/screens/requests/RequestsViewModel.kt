package com.alagamb.petcompose.ui.screens.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alagamb.petcompose.R
import com.alagamb.petcompose.data.model.request.PetRequestItem
import com.alagamb.petcompose.repo.request.RequestRepository
import com.alagamb.petcompose.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RequestsUiState(
    val requests: List<PetRequestItem> = emptyList(),
    val isLoading: Boolean = true
)

sealed interface RequestsIntent {
    data class DeleteRequest(val requestId: String) : RequestsIntent
    data object ClearAll : RequestsIntent
}

sealed interface RequestsSideEffect {
    data class ShowToast(val message: UiText) : RequestsSideEffect
}

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    // State IS a formula over source flows:
    val uiState: StateFlow<RequestsUiState> = requestRepository.getAllRequests()
        .map { list ->
            RequestsUiState(
                requests = list,
                isLoading = false
            )
        }
        .onStart { emit(RequestsUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RequestsUiState(isLoading = true)
        )

    private val _sideEffect = Channel<RequestsSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<RequestsSideEffect> = _sideEffect.receiveAsFlow()

    fun processIntent(intent: RequestsIntent) {
        when (intent) {
            is RequestsIntent.DeleteRequest -> {
                viewModelScope.launch {
                    requestRepository.deleteRequest(intent.requestId)
                    _sideEffect.send(
                        RequestsSideEffect.ShowToast(UiText.StringResource(R.string.request_cancelled))
                    )
                }
            }
            is RequestsIntent.ClearAll -> {
                viewModelScope.launch {
                    requestRepository.clearAllRequests()
                    _sideEffect.send(
                        RequestsSideEffect.ShowToast(UiText.StringResource(R.string.requests_cleared))
                    )
                }
            }
        }
    }
}
