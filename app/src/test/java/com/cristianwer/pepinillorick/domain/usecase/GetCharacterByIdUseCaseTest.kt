package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GetCharacterByIdUseCase].
 */
internal class GetCharacterByIdUseCaseTest {

    private lateinit var useCase: GetCharacterByIdUseCase
    private val repository: CharacterRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetCharacterByIdUseCase(repository)
    }

    @Test
    fun `invoke should return character from repository when ID exists`() = runTest {
        // Given
        val characterId = 1
        val character = mockk<Character>()
        coEvery { repository.getCharacterById(characterId) } returns character

        // When
        val result = useCase(characterId)

        // Then
        assertEquals(character, result)
    }

    @Test
    fun `invoke should return null when character ID does not exist`() = runTest {
        // Given
        val characterId = 999
        coEvery { repository.getCharacterById(characterId) } returns null

        // When
        val result = useCase(characterId)

        // Then
        assertEquals(null, result)
    }
}
