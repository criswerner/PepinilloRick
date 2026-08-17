package com.cristianwer.pepinillorick.ui.favorite_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.usecase.GetFavoriteCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import com.cristianwer.pepinillorick.ui.model.CharacterListState
import com.cristianwer.pepinillorick.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represent the mutually exclusive states of the Favorite List screen.
 */
internal sealed interface FavoriteListUiState {
    data object Loading : FavoriteListUiState
    data object Empty : FavoriteListUiState
    data class Success(val favorites: CharacterListState) : FavoriteListUiState
}

/**
 * ViewModel for the Favorite List screen.
 */
@HiltViewModel
internal class FavoriteListViewModel @Inject constructor(
    private val getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoriteListUiState>(FavoriteListUiState.Loading)
    val uiState: StateFlow<FavoriteListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteCharactersUseCase().collect { list ->
                _uiState.value = if (list.isEmpty()) {
                    FavoriteListUiState.Empty
                } else {
                    FavoriteListUiState.Success(
                        favorites = CharacterListState(items = list.map { it.toUiModel() }.toImmutableList())
                    )
                }
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
