package com.cristianwer.pepinillorick.ui.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.usecase.GetCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.SyncCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import com.cristianwer.pepinillorick.ui.model.CharacterListState
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Character List screen.
 *
 * @property characters Stable wrapper for the list of characters.
 * @property isLoading Whether a network request is in progress.
 * @property error Error message if the request failed.
 */
internal data class CharacterListUiState(
    val characters: CharacterListState = CharacterListState(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel for the Character List screen.
 * 
 * It manages the list of characters and coordinates synchronization with the domain layer.
 */
@HiltViewModel
internal class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val syncCharactersUseCase: SyncCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    init {
        // Single observation of the local database to keep the UI in sync
        viewModelScope.launch {
            getCharactersUseCase().collect { characters ->
                _uiState.update { 
                    it.copy(characters = CharacterListState(items = characters.map { it.toUiModel() })) 
                }
            }
        }
    }

    /**
     * Triggers a request for the next page of characters.
     */
    fun loadCharacters() {
        if (_uiState.value.isLoading) return

        // Update state immediately to prevent race conditions from the UI
        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            syncCharactersUseCase()
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
    
    /**
     * Refreshes the entire list from the first page.
     */
    fun refresh() {
        if (_uiState.value.isLoading) return
        
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            syncCharactersUseCase(forceRefresh = true)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    /**
     * Toggles the favorite status of a character.
     *
     * @param id The unique identifier of the character.
     * @param isFavorite The new favorite status.
     */
    fun toggleFavorite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteUseCase(id, isFavorite)
        }
    }
}
