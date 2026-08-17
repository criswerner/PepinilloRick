package com.cristianwer.pepinillorick.ui.character_list

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.ui.components.CharacterList

/**
 * Screen that displays the list of Rick & Morty characters.
 *
 * @param viewModel The ViewModel providing the UI state.
 * @param onCharacterClick Callback invoked when a character is selected.
 */
@Composable
internal fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            totalItems == 0 || (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1)
        }
    }

    LaunchedEffect(shouldLoadMore.value, uiState.isLoading) {
        if (shouldLoadMore.value && !uiState.isLoading) {
            viewModel.loadCharacters()
        }
    }

    val onFavoriteToggle: (Int, Boolean) -> Unit = remember(viewModel) {
        { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) }
    }

    CharacterList(
        characters = uiState.characters.items,
        onCharacterClick = onCharacterClick,
        onFavoriteToggle = onFavoriteToggle,
        listState = listState,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRetry = viewModel::loadCharacters
    )
}
