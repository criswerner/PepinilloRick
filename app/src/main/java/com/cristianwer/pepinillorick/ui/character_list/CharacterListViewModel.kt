package com.cristianwer.pepinillorick.ui.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.usecase.GetCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.SyncCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import com.cristianwer.pepinillorick.ui.model.CharacterListState
import com.cristianwer.pepinillorick.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represent the mutually exclusive states of the Character List screen.
 */
internal sealed interface CharacterListUiState {
    data object InitialLoading : CharacterListUiState
    data class InitialError(val message: String) : CharacterListUiState
    data class Success(
        val characters: CharacterListState,
        val isPaginating: Boolean = false,
        val paginationError: String? = null
    ) : CharacterListUiState
}

/**
 * Internal helper to track network status independently.
 */
private sealed interface NetworkStatus {
    data object Idle : NetworkStatus
    data object Loading : NetworkStatus
    data class Error(val message: String) : NetworkStatus
}

@HiltViewModel
internal class CharacterListViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase,
    private val syncCharactersUseCase: SyncCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _networkStatus = MutableStateFlow<NetworkStatus>(NetworkStatus.Idle)

    /**
     * Combines database characters and network status into a single, explicit UI state.
     */
    val uiState: StateFlow<CharacterListUiState> = combine(
        getCharactersUseCase().map { list -> list.map { it.toUiModel() } },
        _networkStatus
    ) { characters, network ->
        when {
            characters.isEmpty() && network is NetworkStatus.Loading -> {
                CharacterListUiState.InitialLoading
            }
            characters.isEmpty() && network is NetworkStatus.Error -> {
                CharacterListUiState.InitialError(network.message)
            }
            else -> {
                CharacterListUiState.Success(
                    characters = CharacterListState(items = characters),
                    isPaginating = network is NetworkStatus.Loading,
                    paginationError = (network as? NetworkStatus.Error)?.message
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CharacterListUiState.InitialLoading
    )

    fun loadCharacters() {
        if (_networkStatus.value is NetworkStatus.Loading) return

        _networkStatus.value = NetworkStatus.Loading

        viewModelScope.launch {
            syncCharactersUseCase()
                .onSuccess { _networkStatus.value = NetworkStatus.Idle }
                .onFailure { error -> 
                    _networkStatus.value = NetworkStatus.Error(error.message ?: "Unknown Error") 
                }
        }
    }

    fun toggleFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(id, isFavorite)
        }
    }
}
