package com.cristianwer.pepinillorick.ui.character_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.usecase.GetCharacterByIdUseCase
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
 * @property getCharacterByIdUseCase The use case to fetch character details.
 * @property savedStateHandle Handle to access navigation arguments.
 */
@HiltViewModel
internal class CharacterDetailViewModel @Inject constructor(
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        val characterId: Int? = savedStateHandle["characterId"]
        characterId?.let { loadCharacter(it) }
    }

    /**
     * Loads the character details from the repository.
     *
     * @param id The unique identifier of the character.
     */
    fun loadCharacter(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val character = getCharacterByIdUseCase(id)
            if (character != null) {
                _uiState.update { 
                    it.copy(
                        character = character.toDetailUiModel(),
                        isLoading = false
                    )
                }
            } else {
                // Here we would use a string resource ID if we had one for "not found"
                // _uiState.update { it.copy(isLoading = false, error = R.string.character_detail_not_found) }
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
