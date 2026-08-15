package com.cristianwer.pepinillorick.ui.model

import androidx.compose.runtime.Immutable
import com.cristianwer.pepinillorick.domain.model.Character

/**
 * UI representation of a Rick & Morty character for the list screen.
 */
@Immutable
internal data class CharacterUiModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String,
    val locationName: String
)

/**
 * Maps a domain [Character] to a [CharacterUiModel].
 */
internal fun Character.toUiModel(): CharacterUiModel {
    return CharacterUiModel(
        id = id,
        name = name,
        status = status.value,
        species = species,
        imageUrl = imageUrl,
        locationName = location.name
    )
}

/**
 * UI representation of a Rick & Morty character for the detail screen.
 */
@Immutable
internal data class CharacterDetailUiModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val originName: String,
    val locationName: String,
    val imageUrl: String,
    val episodeCount: Int
)

/**
 * Maps a domain [Character] to a [CharacterDetailUiModel].
 */
internal fun Character.toDetailUiModel(): CharacterDetailUiModel {
    return CharacterDetailUiModel(
        id = id,
        name = name,
        status = status.value,
        species = species,
        gender = gender.value,
        originName = origin.name,
        locationName = location.name,
        imageUrl = imageUrl,
        episodeCount = episodes.size
    )
}
