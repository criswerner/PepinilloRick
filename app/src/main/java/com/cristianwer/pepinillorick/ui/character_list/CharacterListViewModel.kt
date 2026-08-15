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
 */
internal data class CharacterListUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 1,
    val isLastPage: Boolean = false
)

/**
 * ViewModel for the Character List screen managing manual pagination and state.
 */
@HiltViewModel
internal class CharacterListViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase,
    private val syncCharactersUseCase: SyncCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

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
     * Loads the next page of characters.
     */
    fun loadCharacters() {
        if (_uiState.value.isLoading || _uiState.value.isLastPage) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            syncCharactersUseCase(_uiState.value.currentPage)
                .onSuccess {
                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            currentPage = state.currentPage + 1
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }
}
