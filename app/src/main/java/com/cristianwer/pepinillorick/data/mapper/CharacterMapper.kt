package com.cristianwer.pepinillorick.data.mapper

import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import com.cristianwer.platform.data.remote.dto.CharacterDto
import com.cristianwer.platform.data.remote.dto.LocationDto

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

internal fun LocationDto.toDomain(): Location {
    return Location(
        name = name,
        url = url
    )
}
