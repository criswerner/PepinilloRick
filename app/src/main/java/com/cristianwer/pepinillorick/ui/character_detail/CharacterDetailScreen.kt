package com.cristianwer.pepinillorick.ui.character_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.components.CustomAsyncImage
import com.cristianwer.pepinillorick.ui.components.FavoriteButton
import com.cristianwer.pepinillorick.ui.model.CharacterDetailUiModel
import com.cristianwer.pepinillorick.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.character_detail_title).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    val character = (uiState as? CharacterDetailUiState.Success)?.character
                    if (character != null) {
                        FavoriteButton(
                            isFavorite = character.isFavorite,
                            onFavoriteClick = { viewModel.toggleFavorite() },
                            favoriteColor = NeonFavorite
                        )
                    } else {
                        Spacer(modifier = Modifier.width(Dimens.buttonHeight))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TranslucentBlack,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DeepSpace
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SemiTransparentBlack,
                            DeepSpace
                        )
                    )
                )
        ) {
            when (val state = uiState) {
                is CharacterDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = RickGreen
                    )
                }

                is CharacterDetailUiState.Success -> {
                    CharacterDetailContent(character = state.character)
                }

                is CharacterDetailUiState.Error -> {
                    Text(
                        text = stringResource(id = R.string.character_detail_not_found),
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailContent(character: CharacterDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Character Image with Portal Border
        Box(
            modifier = Modifier
                .size(Dimens.characterDetailPortalSize)
                .padding(Dimens.spacingSmall)
                .shadow(
                    elevation = Dimens.shadowLarge,
                    shape = CircleShape,
                    ambientColor = RickGreen,
                    spotColor = RickGreen
                )
                .border(
                    width = Dimens.borderThick,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            RickGreen,
                            PortalGreenLight,
                            PortalGreenDark,
                            RickGreen
                        )
                    ),
                    shape = CircleShape
                )
                .padding(Dimens.spacingExtraSmall)
        ) {
            CustomAsyncImage(
                imageUrl = character.imageUrl,
                contentDescription = character.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(Dimens.spacingLarge))

        // Character Name with Glow
        Text(
            text = character.name.uppercase(),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = Dimens.textLetterSpacing,
                shadow = shadow(color = RickGreen, blurRadius = 10f)
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Text(
            text = "${character.species}, ${character.locationName}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

        // Detail Cards
        DetailItem(
            icon = Icons.Default.Favorite,
            label = stringResource(id = R.string.character_detail_status).uppercase(),
            value = character.status,
            valueColor = if (character.status == "Alive") RickGreen else Color.Red
        )
        DetailItem(
            icon = Icons.Default.Science,
            label = stringResource(id = R.string.character_detail_species).uppercase(),
            value = character.species
        )
        DetailItem(
            icon = when (character.gender) {
                "Male" -> Icons.Default.Male
                "Female" -> Icons.Default.Female
                else -> Icons.Default.Transgender
            },
            label = stringResource(id = R.string.character_detail_gender).uppercase(),
            value = character.gender
        )
        DetailItem(
            icon = Icons.Default.AutoAwesome,
            label = stringResource(id = R.string.character_detail_origin).uppercase(),
            value = character.originName
        )
        DetailItem(
            icon = Icons.Default.LocationOn,
            label = stringResource(id = R.string.character_detail_location).uppercase(),
            value = character.locationName
        )
        DetailItem(
            icon = Icons.Default.Movie,
            label = stringResource(id = R.string.character_detail_episodes).uppercase(),
            value = character.episodeCount.toString(),
            showStar = true
        )

        Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))
    }
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = Color.White,
    showStar: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spacingExtraSmall),
        shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge),
        colors = CardDefaults.cardColors(
            containerColor = TranslucentWhite
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spacingMedium, vertical = Dimens.spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(Dimens.iconSizeMedium)
            )
            Spacer(modifier = Modifier.width(Dimens.spacingMedium))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Visible
            )
            Spacer(modifier = Modifier.width(Dimens.spacingSmall))
            
            // Value part takes remaining space and aligns to end
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showStar) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(Dimens.iconSizeSmall)
                    )
                    Spacer(modifier = Modifier.width(Dimens.spacingExtraSmall))
                }
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = valueColor,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun shadow(color: Color, blurRadius: Float) = Shadow(
    color = color,
    blurRadius = blurRadius
)
