package com.cristianwer.pepinillorick.ui.character_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.domain.model.UiError
import com.cristianwer.pepinillorick.ui.components.CharacterList
import com.cristianwer.pepinillorick.ui.components.CharacterListSkeleton
import com.cristianwer.pepinillorick.ui.mapper.asString
import com.cristianwer.pepinillorick.ui.theme.Dimens
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
internal fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val uiStateState = viewModel.uiState.collectAsStateWithLifecycle()

    val onFavoriteToggle: (Int, Boolean) -> Unit = remember(viewModel) {
        { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) }
    }

    val onRetry = remember(viewModel) {
        { viewModel.loadCharacters() }
    }

    val onLoadMore = remember(viewModel) {
        { viewModel.loadCharacters() }
    }

    CharacterListContent(
        uiStateProvider = { uiStateState.value },
        onCharacterClick = onCharacterClick,
        onFavoriteToggle = onFavoriteToggle,
        onRetry = onRetry,
        onLoadMore = onLoadMore
    )
}

@Composable
private fun CharacterListContent(
    uiStateProvider: () -> CharacterListUiState,
    onCharacterClick: (Int) -> Unit,
    onFavoriteToggle: (Int, Boolean) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()
    val colorScheme = MaterialTheme.colorScheme
    val backgroundBrush = remember(colorScheme) {
        Brush.verticalGradient(
            colors = listOf(
                colorScheme.surface,
                colorScheme.background
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        when (val state = uiStateProvider()) {
            is CharacterListUiState.InitialLoading -> {
                CharacterListSkeleton()

                LaunchedEffect(Unit) {
                    onRetry()
                }
            }
            
            is CharacterListUiState.InitialError -> {
                FullScreenError(error = state.error, onRetry = onRetry)
            }
            
            is CharacterListUiState.Success -> {
                LaunchedEffect(listState, state.isPaginating) {
                    snapshotFlow {
                        val totalItems = listState.layoutInfo.totalItemsCount
                        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                        lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1
                    }
                        .distinctUntilChanged()
                        .filter { it && !state.isPaginating }
                        .collect {
                            onLoadMore()
                        }
                }

                CharacterList(
                    characters = state.characters,
                    onCharacterClick = onCharacterClick,
                    onFavoriteToggle = onFavoriteToggle,
                    listState = listState,
                    isPaginating = state.isPaginating,
                    paginationError = state.paginationError,
                    onRetry = onRetry
                )
            }
        }
    }
}

@Composable
private fun FullScreenError(error: UiError, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.spacingLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error.asString(),
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = stringResource(id = R.string.character_list_retry),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
