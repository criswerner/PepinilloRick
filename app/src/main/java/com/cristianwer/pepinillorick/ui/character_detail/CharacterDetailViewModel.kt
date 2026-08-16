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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI state for the Character Detail screen.
 *
 * @property character The character details to display, or null if not loaded.
 * @property isLoading Whether the details are currently being fetched.
 * @property error An error message if the fetch failed, or null.
 */
internal data class CharacterDetailUiState(
    val character: CharacterDetailUiModel? = null,
    val isLoading: Boolean = false,
    val error: Int? = null
)

/**
 * ViewModel for the Character Detail screen.
 *
 * @property observeCharacterUseCase The use case to observe character details.
 * @property toggleFavoriteUseCase The use case to toggle favorite status.
 * @property savedStateHandle Handle to access navigation arguments.
 */
@HiltViewModel
internal class CharacterDetailViewModel @Inject constructor(
    private val observeCharacterUseCase: ObserveCharacterUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
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
            _uiState.update { it.copy(isLoading = true) }
            observeCharacterUseCase(id).collect { character ->
                _uiState.update { 
                    it.copy(
                        character = character?.toDetailUiModel(),
                        isLoading = false
                    )
                }
            }
        }
    }

    /**
     * Toggles the favorite status of the current character.
     */
    fun toggleFavorite() {
        val character = _uiState.value.character ?: return
        viewModelScope.launch {
            toggleFavoriteUseCase(character.id, !character.isFavorite)
        }
    }
}
