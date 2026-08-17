package com.cristianwer.pepinillorick.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.model.CharacterUiModel
import com.cristianwer.pepinillorick.ui.theme.*

/**
 * Reusable component for displaying a character in a list with Rick & Morty aesthetic.
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spacingExtraSmall)
            .border(
                width = Dimens.borderThin,
                brush = Brush.linearGradient(listOf(RickGreen.copy(alpha = 0.5f), Color.Transparent)),
                shape = RoundedCornerShape(Dimens.cornerRadiusLarge)
            ),
        shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = TranslucentWhite
        ),
        onClick = { onCharacterClick(character.id) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mini Portal Image
            Box(
                modifier = Modifier
                    .size(Dimens.characterItemImageSize)
                    .border(
                        width = Dimens.borderMedium,
                        brush = Brush.sweepGradient(listOf(RickGreen, Color.Cyan, RickGreen)),
                        shape = RoundedCornerShape(Dimens.cornerRadiusMedium)
                    )
                    .padding(Dimens.borderMedium)
            ) {
                CustomAsyncImage(
                    imageUrl = character.imageUrl,
                    contentDescription = character.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                )
            }
            
            Spacer(modifier = Modifier.width(Dimens.spacingMedium))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name.uppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = Dimens.textLetterSpacingSmall
                    ),
                    color = Color.White
                )
                Text(
                    text = "${character.species} - ${character.status}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (character.status == "Alive") RickGreen else Color.LightGray
                )
                
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                
                Text(
                    text = stringResource(id = R.string.character_list_last_location).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = RickGreen.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = character.locationName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 1
                )
            }
            
            FavoriteButton(
                isFavorite = character.isFavorite,
                onFavoriteClick = { onFavoriteToggle(character.id, !character.isFavorite) },
                favoriteColor = NeonFavorite
            )
        }
    }
}
