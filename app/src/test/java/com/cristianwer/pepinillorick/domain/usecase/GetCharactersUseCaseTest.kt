package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.Resource
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GetCharactersUseCase].
 */
internal class GetCharactersUseCaseTest {

    private lateinit var useCase: GetCharactersUseCase
    private val repository: CharacterRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `invoke should return characters flow from repository`() {
        // Given
        val resource = Resource.Success(emptyList<Character>())
        val flow = flowOf(resource)
        every { repository.getCharactersWithSync(any()) } returns flow

        // When
        val result = useCase()

        // Then
        assertEquals(flow, result)
        verify(exactly = 1) { repository.getCharactersWithSync(any()) }
    }
}
