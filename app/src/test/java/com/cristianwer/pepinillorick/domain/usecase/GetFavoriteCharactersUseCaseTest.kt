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
 * Unit tests for [GetFavoriteCharactersUseCase].
 */
internal class GetFavoriteCharactersUseCaseTest {

    private lateinit var useCase: GetFavoriteCharactersUseCase
    private val repository: CharacterRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetFavoriteCharactersUseCase(repository)
    }

    @Test
    fun `invoke should return favorite characters flow from repository`() {
        // Given
        val favorites = emptyList<Character>()
        val flow = flowOf(favorites)
        every { repository.getFavoriteCharacters() } returns flow

        // When
        val result = useCase()

        // Then
        assertEquals(flow, result)
    }
}
