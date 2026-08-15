package com.cristianwer.pepinillorick.ui.model

import androidx.compose.runtime.Immutable
import com.cristianwer.pepinillorick.domain.model.Character

@Immutable
internal data class CharacterUiModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String,
    val locationName: String
)

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
