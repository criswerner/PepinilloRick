package com.cristianwer.pepinillorick.ui.model

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.CharacterGender
import com.cristianwer.pepinillorick.domain.model.CharacterStatus
import com.cristianwer.pepinillorick.domain.model.Location
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Unit tests for UI model mappers.
 */
internal class CharacterUiMapperTest {

    @Test
    fun `Character toUiModel should map correctly`() {
        val domain = Character(
            id = 1,
            name = "Rick",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = CharacterGender.MALE,
            origin = Location("Earth", "url"),
            location = Location("Space C-137", "url"),
            imageUrl = "image_url",
            episodes = listOf("ep1")
        )

        val uiModel = domain.toUiModel()

        assertEquals(domain.id, uiModel.id)
        assertEquals(domain.name, uiModel.name)
        assertEquals("Alive", uiModel.status)
        assertEquals("Space C-137", uiModel.locationName)
    }

    @Test
    fun `Character toDetailUiModel should map correctly`() {
        val domain = Character(
            id = 1,
            name = "Rick",
            status = CharacterStatus.ALIVE,
            species = "Human",
            type = "",
            gender = CharacterGender.MALE,
            origin = Location("Earth", "url"),
            location = Location("Space C-137", "url"),
            imageUrl = "image_url",
            episodes = listOf("ep1", "ep2", "ep3")
        )

        val detailUiModel = domain.toDetailUiModel()

        assertEquals(domain.id, detailUiModel.id)
        assertEquals(domain.name, detailUiModel.name)
        assertEquals("Male", detailUiModel.gender)
        assertEquals("Earth", detailUiModel.originName)
        assertEquals(3, detailUiModel.episodeCount)
    }
}
