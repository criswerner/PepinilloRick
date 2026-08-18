package com.cristianwer.pepinillorick.ui.favorite_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.components.CharacterItemSkeleton
import com.cristianwer.pepinillorick.ui.components.CharacterList
import com.cristianwer.pepinillorick.ui.theme.*

/**
 * Screen that displays the list of favorite Rick & Morty characters.
 */
@Composable
internal fun FavoriteListScreen(
    viewModel: FavoriteListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val backgroundBrush = remember {
        Brush.verticalGradient(
            colors = listOf(SemiTransparentBlack, DeepSpace)
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        when (val state = uiState) {
            is FavoriteListUiState.Loading -> {
                FavoriteLoadingSkeleton()
            }

            is FavoriteListUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.favorite_list_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        textAlign = TextAlign.Center
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

@Composable
private fun FavoriteLoadingSkeleton() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.spacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
        userScrollEnabled = false
    ) {
        items(8) {
            CharacterItemSkeleton()
        }
    }
}
