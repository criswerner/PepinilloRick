package com.cristianwer.pepinillorick.ui.model

import androidx.compose.runtime.Immutable
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus

/**
 * UI representation of a Rick & Morty character for the list screen.
 */
@Immutable
internal data class CharacterUiModel(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val imageUrl: String,
    val locationName: String,
    val isFavorite: Boolean
)

/**
 * Maps a domain [Character] to a [CharacterUiModel].
 */
internal fun Character.toUiModel(): CharacterUiModel {
    return CharacterUiModel(
        id = id,
        name = name,
        status = status,
        species = species,
        imageUrl = imageUrl,
        locationName = location.name,
        isFavorite = isFavorite
    )
}

/**
 * UI representation of a Rick & Morty character for the detail screen.
 */
@Immutable
internal data class CharacterDetailUiModel(
    val id: Int,
    val name: String,
    val status: CharacterStatus,
    val species: String,
    val gender: CharacterGender,
    val originName: String,
    val locationName: String,
    val imageUrl: String,
    val episodeCount: Int,
    val isFavorite: Boolean
)

/**
 * Maps a domain [Character] to a [CharacterDetailUiModel].
 */
internal fun Character.toDetailUiModel(): CharacterDetailUiModel {
    return CharacterDetailUiModel(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        originName = origin.name,
        locationName = location.name,
        imageUrl = imageUrl,
        episodeCount = episodes.size,
        isFavorite = isFavorite
    )
}
