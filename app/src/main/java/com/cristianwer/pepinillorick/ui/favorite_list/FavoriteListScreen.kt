package com.cristianwer.pepinillorick.ui.favorite_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.components.CharacterList

/**
 * Screen that displays the list of favorite Rick & Morty characters.
 */
@Composable
internal fun FavoriteListScreen(
    viewModel: FavoriteListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState) {
            is FavoriteListUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is FavoriteListUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.favorite_list_empty),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is FavoriteListUiState.Success -> {
                CharacterList(
                    characters = state.favorites,
                    onCharacterClick = onCharacterClick,
                    onFavoriteToggle = { id, _ -> viewModel.toggleFavorite(id, false) }
                )
            }
        }
    }
}
