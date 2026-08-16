package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ObserveCharacterUseCase].
 */
internal class ObserveCharacterUseCaseTest {

    private lateinit var useCase: ObserveCharacterUseCase
    private val repository: CharacterRepository = mockk()

    @Before
    fun setUp() {
        useCase = ObserveCharacterUseCase(repository)
    }

    @Test
    fun `invoke should return character flow from repository`() {
        // Given
        val characterId = 1
        val character = mockk<Character>()
        val flow = flowOf(character)
        every { repository.getCharacterByIdFlow(characterId) } returns flow

        // When
        val result = useCase(characterId)

        // Then
        assertEquals(flow, result)
    }
}
