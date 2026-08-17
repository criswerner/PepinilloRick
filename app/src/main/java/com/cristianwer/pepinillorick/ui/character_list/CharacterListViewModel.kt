package com.cristianwer.pepinillorick.ui.character_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cristianwer.pepinillorick.domain.model.Resource
import com.cristianwer.pepinillorick.domain.usecase.GetCharactersUseCase
import com.cristianwer.pepinillorick.domain.usecase.ToggleFavoriteUseCase
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
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
        val characters: ImmutableList<CharacterUiModel>,
        val isPaginating: Boolean = false,
        val paginationError: String? = null
    ) : CharacterListUiState
}

/**
 * Internal event to trigger character loading.
 * @property forceRefresh Whether to clear cache and start from page 1.
 * @property timestamp Unique identifier to ensure each trigger is processed.
 */
private data class LoadEvent(
    val forceRefresh: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@HiltViewModel
internal class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    // Trigger for loading characters, uses an object to ensure distinct values even for the same flag
    private val loadTrigger = MutableStateFlow(LoadEvent(forceRefresh = false))

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<CharacterListUiState> = loadTrigger
        .flatMapLatest { event ->
            getCharactersUseCase(event.forceRefresh)
        }
        .map { resource ->
            val characters = resource.data?.map { it.toUiModel() }?.toImmutableList() ?: persistentListOf()

            when (resource) {
                is Resource.Loading -> {
                    if (characters.isEmpty()) {
                        CharacterListUiState.InitialLoading
                    } else {
                        CharacterListUiState.Success(characters, isPaginating = true)
                    }
                }
                is Resource.Success -> {
                    CharacterListUiState.Success(characters)
                }
                is Resource.Error -> {
                    if (characters.isEmpty()) {
                        CharacterListUiState.InitialError(resource.message ?: "Unknown Error")
                    } else {
                        CharacterListUiState.Success(
                            characters = characters,
                            paginationError = resource.message
                        )
                    }
                }
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CharacterListUiState.InitialLoading
        )

    /**
     * Loads characters. 
     *
     * @param forceRefresh If true, it clears the local database and starts from page 1.
     */
    fun loadCharacters(forceRefresh: Boolean = false) {
        val currentState = uiState.value
        
        // Prevent concurrent requests unless it's a force refresh or a retry from error
        val isPaginating = currentState is CharacterListUiState.Success && currentState.isPaginating
        val isInitialLoading = currentState is CharacterListUiState.InitialLoading
        
        if (!forceRefresh && (isPaginating || isInitialLoading)) return
        
        loadTrigger.value = LoadEvent(forceRefresh = forceRefresh)
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
