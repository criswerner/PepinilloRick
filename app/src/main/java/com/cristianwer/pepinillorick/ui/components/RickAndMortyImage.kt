package com.cristianwer.pepinillorick.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.cristianwer.pepinillorick.R

/**
 * A reusable image component for the Rick & Morty app that handles loading and error states
 * with a consistent placeholder.
 *
 * @param imageUrl The URL of the image to load.
 * @param contentDescription The accessibility description for the image.
 * @param modifier The modifier to be applied to the image.
 * @param contentScale How to scale the image within its bounds.
 */
@Composable
internal fun RickAndMortyImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val placeholder = painterResource(id = R.drawable.placeholder)
    
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        contentScale = contentScale
    )
}
