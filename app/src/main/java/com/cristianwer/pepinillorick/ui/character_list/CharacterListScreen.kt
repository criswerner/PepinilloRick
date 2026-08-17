package com.cristianwer.pepinillorick.ui.character_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.components.CharacterList
import com.cristianwer.pepinillorick.ui.theme.DeepSpace
import com.cristianwer.pepinillorick.ui.theme.Dimens
import com.cristianwer.pepinillorick.ui.theme.RickGreen
import com.cristianwer.pepinillorick.ui.theme.SemiTransparentBlack

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

    val onRetry = remember(viewModel) {
        { viewModel.loadCharacters() }
    }

    // High-level decision based on explicit states from ViewModel
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        SemiTransparentBlack,
                        DeepSpace
                    )
                )
            )
    ) {
        when (val state = uiState) {
            is CharacterListUiState.InitialLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(Dimens.loadingIndicatorSize),
                        color = RickGreen
                    )
                }
                // Disparo de carga inicial si estamos en este estado
                LaunchedEffect(Unit) { viewModel.loadCharacters() }
            }
            
            is CharacterListUiState.InitialError -> {
                FullScreenError(onRetry = onRetry)
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
                    characterListState = state.characters,
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
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = RickGreen)
        ) {
            Text(text = stringResource(id = R.string.character_list_retry), color = DeepSpace)
        }
    }
}
