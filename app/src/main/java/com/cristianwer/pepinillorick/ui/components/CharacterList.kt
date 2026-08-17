package com.cristianwer.pepinillorick.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.theme.Dimens

/**
 * A reusable list of Rick & Morty characters.
 * It focuses strictly on rendering the collection and pagination footers.
 *
 * @param characters List of characters to display.
 * @param onCharacterClick Callback for item click.
 * @param onFavoriteToggle Callback for favorite toggle.
 * @param modifier Modifier for the list.
 * @param listState State for the LazyColumn.
 * @param isPaginating Whether a pagination request is in progress (footer).
 * @param paginationError Optional error message for pagination (footer).
 * @param onRetry Callback for the retry button.
 */
@Composable
internal fun CharacterList(
    characters: List<CharacterUiModel>,
    onCharacterClick: (Int) -> Unit,
    onFavoriteToggle: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    isPaginating: Boolean = false,
    paginationError: String? = null,
    onRetry: () -> Unit = {}
) {
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.spacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium)
    ) {
        items(
            items = characters,
            key = { it.id },
            contentType = { "character" }
        ) { character ->
            CharacterItem(
                character = character,
                onCharacterClick = onCharacterClick,
                onFavoriteToggle = onFavoriteToggle
            )
        }

        // Pagination footer states
        if (isPaginating) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMedium),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(Dimens.loadingIndicatorSize))
                }
            }
        }

        if (paginationError != null) {
            item {
                ErrorItem(onRetry = onRetry)
            }
        }
    }
}

@Composable
private fun ErrorItem(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(Dimens.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.character_list_error),
            color = MaterialTheme.colorScheme.error
        )
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.character_list_retry))
        }
    }
}
