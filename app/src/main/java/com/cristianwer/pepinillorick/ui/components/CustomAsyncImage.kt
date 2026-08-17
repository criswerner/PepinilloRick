package com.cristianwer.pepinillorick.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.cristianwer.pepinillorick.R

/**
 * A reusable asynchronous image component that handles loading and error states
 * with a consistent placeholder and a crossfade animation.
 *
 * @param imageUrl The URL of the image to load.
 * @param contentDescription The accessibility description for the image.
 * @param modifier The modifier to be applied to the image.
 * @param contentScale How to scale the image within its bounds.
 */
@Composable
internal fun CustomAsyncImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val imageRequest = remember(imageUrl) {
        ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build()
    }

    val placeholder = painterResource(id = R.drawable.placeholder)
    
    AsyncImage(
        model = imageRequest,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        contentScale = contentScale
    )
}
