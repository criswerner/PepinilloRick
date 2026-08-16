package com.cristianwer.pepinillorick.ui.character_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.cristianwer.pepinillorick.R
import com.cristianwer.pepinillorick.ui.model.CharacterDetailUiModel

/**
 * Screen that displays the detailed information of a Rick & Morty character.
 *
 * @param viewModel The ViewModel providing the UI state.
 * @param onBackClick Callback invoked when the user clicks the back button.
 */
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
                title = { Text(text = stringResource(id = R.string.character_detail_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    uiState.character?.let { character ->
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (character.isFavorite) {
                                    stringResource(id = R.string.character_list_favorite)
                                } else {
                                    stringResource(id = R.string.character_list_not_favorite)
                                },
                                tint = if (character.isFavorite) Color.Red else LocalContentColor.current
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            uiState.character?.let { character ->
                CharacterDetailContent(character = character)
            }

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (uiState.error != null || (uiState.character == null && !uiState.isLoading)) {
                Text(
                    text = stringResource(id = R.string.character_detail_not_found),
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Content of the character detail screen.
 *
 * @param character The character detail information.
 */
@Composable
private fun CharacterDetailContent(character: CharacterDetailUiModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = character.imageUrl,
            contentDescription = character.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            DetailRow(
                label = stringResource(id = R.string.character_detail_status),
                value = character.status
            )
            DetailRow(
                label = stringResource(id = R.string.character_detail_species),
                value = character.species
            )
            DetailRow(
                label = stringResource(id = R.string.character_detail_gender),
                value = character.gender
            )
            DetailRow(
                label = stringResource(id = R.string.character_detail_origin),
                value = character.originName
            )
            DetailRow(
                label = stringResource(id = R.string.character_detail_location),
                value = character.locationName
            )
            DetailRow(
                label = stringResource(id = R.string.character_detail_episodes),
                value = character.episodeCount.toString()
            )
        }
    }
}

/**
 * A row displaying a label and a value.
 */
@Composable
private fun DetailRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}
