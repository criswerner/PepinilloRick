package com.cristianwer.pepinillorick.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.cristianwer.pepinillorick.R

/**
 * Representation of an item in the bottom navigation bar.
 *
 * @property route The navigation route associated with this item.
 * @property titleRes The string resource ID for the item's title.
 * @property icon The icon to display for this item.
 */
internal sealed class BottomNavItem(
    val route: String,
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    data object Characters : BottomNavItem(
        route = "character_list",
        titleRes = R.string.character_list_title,
        icon = Icons.Default.Person
    )

    data object Favorites : BottomNavItem(
        route = "favorite_list",
        titleRes = R.string.favorite_list_title,
        icon = Icons.Default.Favorite
    )
}
