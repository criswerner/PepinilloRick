package com.cristianwer.pepinillorick.ui.favorite_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
 *
 * @param viewModel The ViewModel providing the UI state.
 * @param onCharacterClick Callback invoked when a character is selected.
 */
@Composable
internal fun FavoriteListScreen(
    viewModel: FavoriteListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterList(
        characters = uiState.favorites,
        onCharacterClick = onCharacterClick,
        onFavoriteToggle = { id, _ -> viewModel.toggleFavorite(id, false) },
        emptyPlaceholder = {
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
    )
}
