package com.cristianwer.pepinillorick.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.cristianwer.pepinillorick.ui.theme.Dimens

/**
 * A skeleton loader that displays a list of character item placeholders.
 * Use this for initial loading states on lists.
 *
 * @param modifier The modifier to be applied to the list.
 * @param itemCount Number of skeleton items to display.
 */
@Composable
internal fun CharacterListSkeleton(
    modifier: Modifier = Modifier,
    itemCount: Int = 8
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.spacingMedium),
        verticalArrangement = Arrangement.spacedBy(Dimens.spacingMedium),
        userScrollEnabled = false
    ) {
        items(itemCount) {
            CharacterItemSkeleton()
        }
    }
}

/**
 * A skeleton loader representing a single character item card.
 *
 * @param modifier The modifier to be applied to the card.
 */
@Composable
internal fun CharacterItemSkeleton(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.spacingExtraSmall),
        shape = RoundedCornerShape(Dimens.cornerRadiusLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .size(Dimens.characterItemImageSize)
                    .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                    .shimmerEffect()
            )
            
            Spacer(modifier = Modifier.width(Dimens.spacingMedium))
            
            Column(modifier = Modifier.weight(1f)) {
                // Name Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(Dimens.cornerRadiusExtraSmall))
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.height(Dimens.spacingSmall))
                
                // Status/Species Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(Dimens.cornerRadiusExtraSmall))
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.height(Dimens.spacingMedium))
                
                // Location Label Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(Dimens.cornerRadiusExtraSmall))
                        .shimmerEffect()
                )
                
                Spacer(modifier = Modifier.height(Dimens.spacingExtraSmall))
                
                // Location Value Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(Dimens.cornerRadiusExtraSmall))
                        .shimmerEffect()
                )
            }
            
            // Favorite Button Placeholder
            Box(
                modifier = Modifier
                    .size(Dimens.iconSizeMedium)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}
