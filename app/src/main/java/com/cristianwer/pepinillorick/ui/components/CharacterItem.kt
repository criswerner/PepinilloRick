package com.cristianwer.pepinillorick.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.theme.Dimens

/**
 * Reusable component for displaying a character in a list.
 *
 * @param character The character data to display.
 * @param onCharacterClick Callback when the card is clicked.
 * @param onFavoriteToggle Callback when the favorite button is clicked.
 */
@Composable
internal fun CharacterItem(
    character: CharacterUiModel,
    onCharacterClick: (Int) -> Unit,
    onFavoriteToggle: (Int, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.cardElevation),
        onClick = { onCharacterClick(character.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomAsyncImage(
                imageUrl = character.imageUrl,
                contentDescription = character.name,
                modifier = Modifier.size(Dimens.characterItemImageSize)
            )
            Spacer(modifier = Modifier.width(Dimens.spacingMedium))
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
            FavoriteButton(
                isFavorite = character.isFavorite,
                onFavoriteClick = { onFavoriteToggle(character.id, !character.isFavorite) }
            )
        }
    }
}
