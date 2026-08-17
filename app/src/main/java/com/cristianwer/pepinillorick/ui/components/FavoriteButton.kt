package com.cristianwer.pepinillorick.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.theme.RickGreen

/**
 * A reusable favorite button component.
 *
 * @param isFavorite Whether the item is currently marked as favorite.
 * @param onFavoriteClick Callback invoked when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 * @param favoriteColor The color of the icon when it's marked as favorite.
 */
@Composable
internal fun FavoriteButton(
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    favoriteColor: Color = RickGreen
) {
    IconButton(
        onClick = onFavoriteClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) {
                stringResource(id = R.string.character_list_favorite)
            } else {
                stringResource(id = R.string.character_list_not_favorite)
            },
            tint = if (isFavorite) favoriteColor else LocalContentColor.current
        )
    }
}
