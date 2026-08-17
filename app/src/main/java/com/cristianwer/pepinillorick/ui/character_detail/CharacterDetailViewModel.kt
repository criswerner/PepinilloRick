package com.cristianwer.pepinillorick.ui.character_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.usecase.ObserveCharacterUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import com.cristianwer.pepinillorick.ui.model.CharacterDetailUiModel
import com.cristianwer.pepinillorick.ui.model.toDetailUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represent the mutually exclusive states of the Character Detail screen.
 */
internal sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState
    data class Success(val character: CharacterDetailUiModel) : CharacterDetailUiState
    data object Error : CharacterDetailUiState
}

/**
 * ViewModel for the Character Detail screen.
 */
@HiltViewModel
internal class CharacterDetailViewModel @Inject constructor(
    private val observeCharacterUseCase: ObserveCharacterUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        val characterId: Int? = savedStateHandle["characterId"]
        characterId?.let { observeCharacter(it) }
    }

    /**
     * Observes the character details from the repository.
     *
     * @param id The unique identifier of the character.
     */
    private fun observeCharacter(id: Int) {
        viewModelScope.launch {
            _uiState.value = CharacterDetailUiState.Loading
            observeCharacterUseCase(id).collect { character ->
                _uiState.value = if (character != null) {
                    CharacterDetailUiState.Success(character = character.toDetailUiModel())
                } else {
                    CharacterDetailUiState.Error
                }
            }
        }
    }

    /**
     * Toggles the favorite status of the current character.
     */
    fun toggleFavorite() {
        val currentState = _uiState.value
        if (currentState is CharacterDetailUiState.Success) {
            val character = currentState.character
            viewModelScope.launch {
                toggleFavoriteUseCase(character.id, !character.isFavorite)
            }
        }
    }
}
