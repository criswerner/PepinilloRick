package com.cristianwer.pepinillorick.data.mapper

import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.platform.data.remote.dto.CharacterDto
import com.cristianwer.platform.data.remote.dto.LocationDto
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for Character mappers.
 */
internal class CharacterMapperTest {

    private val locationDto = LocationDto(name = "Earth", url = "url")
    private val characterDto = CharacterDto(
        id = 1,
        name = "Rick",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        origin = locationDto,
        location = locationDto,
        image = "image_url",
        episode = listOf("ep1", "ep2"),
        url = "url",
        created = "date"
    )

    @Test
    fun `CharacterDto toDomain should map correctly`() {
        val domain = characterDto.toDomain()

        assertEquals(characterDto.id, domain.id)
        assertEquals(characterDto.name, domain.name)
        assertEquals(CharacterStatus.ALIVE, domain.status)
        assertEquals(CharacterGender.MALE, domain.gender)
        assertEquals(characterDto.origin.name, domain.origin.name)
        assertEquals(characterDto.episode, domain.episodes)
    }

    @Test
    fun `CharacterDto toEntity should map correctly`() {
        val entity = characterDto.toEntity()

        assertEquals(characterDto.id, entity.id)
        assertEquals(characterDto.name, entity.name)
        assertEquals(characterDto.status, entity.status)
        assertEquals("ep1,ep2", entity.episodes)
    }

    @Test
    fun `CharacterEntity toDomain should map correctly`() {
        val entity = CharacterEntity(
            id = 1,
            name = "Rick",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            originName = "Earth",
            originUrl = "url",
            locationName = "Earth",
            locationUrl = "url",
            imageUrl = "image_url",
            episodes = "ep1,ep2"
        )

        val domain = entity.toDomain()

        assertEquals(entity.id, domain.id)
        assertEquals(listOf("ep1", "ep2"), domain.episodes)
        assertEquals(CharacterStatus.ALIVE, domain.status)
    }
}
