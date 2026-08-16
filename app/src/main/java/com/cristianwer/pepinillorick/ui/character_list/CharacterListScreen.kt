package com.cristianwer.pepinillorick.ui.character_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel

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

    // Detect when we need more data
    val shouldLoadMore = remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            totalItems == 0 || (lastVisibleItem != null && lastVisibleItem.index >= totalItems - 1)
        }
    }

    LaunchedEffect(shouldLoadMore.value, uiState.isLoading) {
        if (shouldLoadMore.value && !uiState.isLoading) {
            // Use character count as a simple check to avoid initial trigger if already loading
            viewModel.loadCharacters()
        }
    }

    // Stable toggle lambda to prevent full list recomposition
    val onFavoriteToggle: (Int, Boolean) -> Unit = remember(viewModel) {
        { id, isFavorite -> viewModel.toggleFavorite(id, isFavorite) }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = uiState.characters.items,
                key = { it.id },
                contentType = { "character" }
            ) { character ->
                CharacterItem(
                    character = character,
                    onCharacterClick = onCharacterClick,
                    onFavoriteToggle = onFavoriteToggle
                )
            }

            if (uiState.isLoading) {
                item {
                    LoadingIndicator(modifier = Modifier.fillMaxWidth())
                }
            }

            if (uiState.error != null) {
                item {
                    ErrorRetryItem(
                        message = stringResource(id = R.string.character_list_error),
                        onRetry = viewModel::loadCharacters
                    )
                }
            }
        }
    }
}

/**
 * UI component for a single character item.
 */
@Composable
private fun CharacterItem(
    character: CharacterUiModel,
    onCharacterClick: (Int) -> Unit,
    onFavoriteToggle: (Int, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onCharacterClick(character.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${character.species} - ${character.status}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(id = R.string.character_list_last_location),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = character.locationName,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(
                onClick = { onFavoriteToggle(character.id, !character.isFavorite) }
            ) {
                Icon(
                    imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (character.isFavorite) Color.Red else LocalContentColor.current
                )
            }
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun ErrorRetryItem(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.character_list_retry))
        }
    }
}
