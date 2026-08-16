package com.cristianwer.pepinillorick.ui.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.usecase.GetCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.SyncCharactersUseCase
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Character List screen.
 *
 * @property isLoading Whether a network request is in progress.
 * @property error Error message if the request failed.
 */
internal data class CharacterListUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel for the Character List screen.
 * 
 * It coordinates data retrieval from the domain layer and manages pagination triggers.
 */
@HiltViewModel
internal class CharacterListViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase,
    private val syncCharactersUseCase: SyncCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    /**
     * List of characters observed from the local database.
     */
    val characters: StateFlow<List<CharacterUiModel>> = getCharactersUseCase()
        .map { list -> list.map { it.toUiModel() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadCharacters()
    }

    /**
     * Triggers a request for the next page of characters.
     * The repository handles the current pagination state.
     */
    fun loadCharacters() {
        if (_uiState.value.isLoading) return

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
}
