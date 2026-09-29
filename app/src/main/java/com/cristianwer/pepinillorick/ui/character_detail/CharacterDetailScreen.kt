package com.cristianwer.pepinillorick.ui.character_detail

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.components.CustomAsyncImage
import com.cristianwer.pepinillorick.ui.components.FavoriteButton
import com.cristianwer.pepinillorick.ui.components.FloatingHeartsOverlay
import com.cristianwer.pepinillorick.ui.mapper.getColor
import com.cristianwer.pepinillorick.ui.mapper.getIcon
import com.cristianwer.pepinillorick.ui.model.CharacterDetailUiModel
import com.cristianwer.pepinillorick.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

@Composable
internal fun CharacterDetailScreen(
    viewModel: CharacterDetailViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CharacterDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteToggle = viewModel::toggleFavorite
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CharacterDetailContent(
    uiState: CharacterDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val character = (uiState as? CharacterDetailUiState.Success)?.character
    var favoriteAnimationTrigger by remember { mutableStateOf(0) }
    var previousIsFavorite by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(character?.isFavorite) {
        val isFav = character?.isFavorite
        if (isFav == true && previousIsFavorite == false) {
            favoriteAnimationTrigger += 1
        }
        if (isFav != null) {
            previousIsFavorite = isFav
        }
    }

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
                            tint = colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    if (character != null) {
                        FavoriteButton(
                            isFavorite = character.isFavorite,
                            onFavoriteClick = onFavoriteToggle,
                            favoriteColor = colorScheme.primary
                        )
                    } else {
                        Spacer(modifier = Modifier.width(Dimens.buttonHeight))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.surface.copy(alpha = 0.5f),
                    titleContentColor = colorScheme.onSurface
                )
            )
        },
        containerColor = colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            colorScheme.surface.copy(alpha = 0.8f),
                            colorScheme.background
                        )
                    )
                )
        ) {
            when (val state = uiState) {
                is CharacterDetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = colorScheme.primary
                    )
                }

                is CharacterDetailUiState.Success -> {
                    CharacterDetailSuccessContent(character = state.character)
                }

                is CharacterDetailUiState.Error -> {
                    Text(
                        text = stringResource(id = R.string.character_detail_not_found),
                        modifier = Modifier.align(Alignment.Center),
                        color = colorScheme.error
                    )
                }
            }

            FloatingHeartsOverlay(
                triggerKey = favoriteAnimationTrigger,
                modifier = Modifier.fillMaxSize(),
                primaryColor = colorScheme.primary
            )
        }
    }
}

@Composable
private fun CharacterDetailSuccessContent(character: CharacterDetailUiModel) {
    val colorScheme = MaterialTheme.colorScheme

    val avatarScale = remember { Animatable(0.1f) }
    val avatarAlpha = remember { Animatable(0f) }
    val labelProgress = remember { Animatable(0f) }
    val valueAlpha = remember { Animatable(0f) }

    LaunchedEffect(character.id) {
        avatarScale.snapTo(0.1f)
        avatarAlpha.snapTo(0f)
        labelProgress.snapTo(0f)
        valueAlpha.snapTo(0f)

        launch {
            avatarAlpha.animateTo(1f, animationSpec = tween(300))
        }
        launch {
            avatarScale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }

        delay(150)

        labelProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing)
        )

        valueAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 350)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimens.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.characterDetailPortalSize)
                .padding(Dimens.spacingSmall)
                .graphicsLayer {
                    scaleX = avatarScale.value
                    scaleY = avatarScale.value
                    alpha = avatarAlpha.value
                }
                .shadow(
                    elevation = Dimens.shadowLarge,
                    shape = CircleShape,
                    ambientColor = colorScheme.primary,
                    spotColor = colorScheme.primary
                )
                .border(
                    width = Dimens.borderThick,
                    brush = Brush.sweepGradient(
                        colors = listOf(
                            colorScheme.primary,
                            PortalGreenLight,
                            PortalGreenDark,
                            colorScheme.primary
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

        NameWithGlowCharacter(
            character = character,
            labelProgress = labelProgress.value,
            valueAlpha = valueAlpha.value
        )

        Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))

        DetailCardCharacter(
            character = character,
            labelProgress = labelProgress.value,
            valueAlpha = valueAlpha.value
        )

        Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))
    }
}

@Composable
private fun NameWithGlowCharacter(
    character: CharacterDetailUiModel,
    labelProgress: Float,
    valueAlpha: Float
) {
    val colorScheme = MaterialTheme.colorScheme
    Text(
        text = character.name.uppercase(),
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = Dimens.textLetterSpacing,
            shadow = Shadow(color = colorScheme.primary, blurRadius = 10f)
        ),
        color = colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.graphicsLayer {
            translationX = (-50.dp * (1f - labelProgress)).toPx()
            alpha = labelProgress
        }
    )

    Text(
        text = "${character.species}, ${character.locationName}",
        style = MaterialTheme.typography.bodyMedium,
        color = colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier.graphicsLayer {
            alpha = valueAlpha
        }
    )
}

@Composable
private fun DetailCardCharacter(
    character: CharacterDetailUiModel,
    labelProgress: Float,
    valueAlpha: Float
) {
    val colorScheme = MaterialTheme.colorScheme
    DetailItem(
        icon = Icons.Default.Favorite,
        label = stringResource(id = R.string.character_detail_status).uppercase(),
        value = character.status.value,
        valueColor = character.status.getColor(colorScheme),
        labelProgress = labelProgress,
        valueAlpha = valueAlpha
    )
    DetailItem(
        icon = Icons.Default.Science,
        label = stringResource(id = R.string.character_detail_species).uppercase(),
        value = character.species,
        labelProgress = labelProgress,
        valueAlpha = valueAlpha
    )
    DetailItem(
        icon = character.gender.getIcon(),
        label = stringResource(id = R.string.character_detail_gender).uppercase(),
        value = character.gender.value,
        labelProgress = labelProgress,
        valueAlpha = valueAlpha
    )
    DetailItem(
        icon = Icons.Default.AutoAwesome,
        label = stringResource(id = R.string.character_detail_origin).uppercase(),
        value = character.originName,
        labelProgress = labelProgress,
        valueAlpha = valueAlpha
    )
    DetailItem(
        icon = Icons.Default.LocationOn,
        label = stringResource(id = R.string.character_detail_location).uppercase(),
        value = character.locationName,
        labelProgress = labelProgress,
        valueAlpha = valueAlpha
    )
    DetailItem(
        icon = Icons.Default.Movie,
        label = stringResource(id = R.string.character_detail_episodes).uppercase(),
        value = character.episodeCount.toString(),
        showStar = true,
        labelProgress = labelProgress,
        valueAlpha = valueAlpha
    )
}

@Composable
private fun DetailItem(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    showStar: Boolean = false,
    labelProgress: Float = 1f,
    valueAlpha: Float = 1f
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.spacingExtraSmall),
        shape = RoundedCornerShape(Dimens.cornerRadiusExtraLarge),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surfaceVariant.copy(alpha = 0.2f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.spacingMedium, vertical = Dimens.spacingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.graphicsLayer {
                    translationX = (-40.dp * (1f - labelProgress)).toPx()
                    alpha = labelProgress
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colorScheme.primary,
                    modifier = Modifier.size(Dimens.iconSizeMedium)
                )
                Spacer(modifier = Modifier.width(Dimens.spacingMedium))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Visible
                )
            }
            Spacer(modifier = Modifier.width(Dimens.spacingSmall))
            
            Row(
                modifier = Modifier
                    .weight(1f)
                    .graphicsLayer {
                        alpha = valueAlpha
                    },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showStar) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = colorScheme.primary,
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
