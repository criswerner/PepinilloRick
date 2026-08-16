package com.cristianwer.pepinillorick.ui.favorite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.usecase.GetFavoriteCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
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
 * UI state for the Favorite List screen.
 *
 * @property favorites The list of favorite characters.
 */
internal data class FavoriteListUiState(
    val favorites: List<CharacterUiModel> = emptyList()
)

/**
 * ViewModel for the Favorite List screen.
 * 
 * It manages the reactive observation of favorite characters from the domain layer.
 */
@HiltViewModel
internal class FavoriteListViewModel @Inject constructor(
    private val getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteListUiState())
    val uiState: StateFlow<FavoriteListUiState> = _uiState.asStateFlow()

    init {
        // Observe favorite characters from the local database
        viewModelScope.launch {
            getFavoriteCharactersUseCase().collect { favorites ->
                _uiState.update { it.copy(favorites = favorites.map { it.toUiModel() }) }
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
