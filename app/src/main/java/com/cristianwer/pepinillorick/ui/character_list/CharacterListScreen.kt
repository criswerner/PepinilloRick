package com.cristianwer.pepinillorick.ui.character_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.components.CharacterList
import com.cristianwer.pepinillorick.ui.theme.Dimens

@Composable
internal fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    val onFavoriteToggle: (Int, Boolean) -> Unit = remember(viewModel) {
        { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) }
    }

    // High-level decision based on explicit states from ViewModel
    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is CharacterListUiState.InitialLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(Dimens.loadingIndicatorSize))
                }
                // Disparo de carga inicial si estamos en este estado
                LaunchedEffect(Unit) { viewModel.loadCharacters() }
            }
            
            is CharacterListUiState.InitialError -> {
                FullScreenError(onRetry = viewModel::loadCharacters)
            }
            
            is CharacterListUiState.Success -> {
                // Pagination trigger
                val shouldLoadMore = remember {
                    derivedStateOf {
                        val totalItems = listState.layoutInfo.totalItemsCount
                        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                        lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1
                    }
                }

                LaunchedEffect(shouldLoadMore.value) {
                    if (shouldLoadMore.value && !state.isPaginating) {
                        viewModel.loadCharacters()
                    }
                }

                CharacterList(
                    characters = state.characters.items,
                    onCharacterClick = onCharacterClick,
                    onFavoriteToggle = onFavoriteToggle,
                    listState = listState,
                    isPaginating = state.isPaginating,
                    paginationError = state.paginationError,
                    onRetry = viewModel::loadCharacters
                )
            }
        }
    }
}

@Composable
private fun FullScreenError(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.spacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.character_list_error),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.character_list_retry))
        }
    }
}
