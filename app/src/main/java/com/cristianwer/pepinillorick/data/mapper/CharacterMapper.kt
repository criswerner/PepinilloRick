package com.cristianwer.pepinillorick.data.mapper

import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import com.cristianwer.platform.data.remote.dto.CharacterDto
import com.cristianwer.platform.data.remote.dto.LocationDto

/**
 * Maps a [CharacterDto] from the data layer to a [Character] domain model.
 *
 * @return A [Character] domain object with parsed status, gender, and nested location data.
 */
internal fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = CharacterStatus.fromString(status),
        species = species,
        type = type,
        gender = CharacterGender.fromString(gender),
        origin = origin.toDomain(),
        location = location.toDomain(),
        imageUrl = image,
        episodes = episode
    )
}

/**
 * Maps a [CharacterDto] received from the network to a [CharacterEntity] for local storage.
 *
 * This function flattens nested objects (like origin and location) and converts the list
 * of episodes into a comma-separated string to be stored in the database.
 *
 * @return A [CharacterEntity] populated with the data from the DTO.
 */
internal fun CharacterDto.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = id,
        name = name,
        status = status,
        species = species,
        type = type,
        gender = gender,
        originName = origin.name,
        originUrl = origin.url,
        locationName = location.name,
        locationUrl = location.url,
        imageUrl = image,
        episodes = episode.joinToString(",")
    )
}

/**
 * Maps a [CharacterEntity] from the local database to a [Character] domain model.
 *
 * @return A domain representation of the character, converting stored primitive types
 * and comma-separated strings back into structured domain objects and lists.
 */
internal fun CharacterEntity.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        status = CharacterStatus.fromString(status),
        species = species,
        type = type,
        gender = CharacterGender.fromString(gender),
        origin = Location(originName, originUrl),
        location = Location(locationName, locationUrl),
        imageUrl = imageUrl,
        episodes = episodes.split(",").filter { it.isNotBlank() }
    )
}

/**
 * Maps a [LocationDto] data transfer object from the data layer to a [Location]
 * domain model object.
 *
 * @return A [Location] instance containing the name and URL of the location.
 */
internal fun LocationDto.toDomain(): Location {
    return Location(
        name = name,
        url = url
    )
}
