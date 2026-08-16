package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [ToggleFavoriteUseCase].
 */
internal class ToggleFavoriteUseCaseTest {

    private lateinit var useCase: ToggleFavoriteUseCase
    private val repository: CharacterRepository = mockk()

    @Before
    fun setUp() {
        useCase = ToggleFavoriteUseCase(repository)
    }

    @Test
    fun `invoke should delegate toggle to repository`() = runTest {
        // Given
        val characterId = 1
        val isFavorite = true
        coEvery { repository.toggleFavorite(characterId, isFavorite) } returns Unit

        // When
        useCase(characterId, isFavorite)

        // Then
        coVerify(exactly = 1) { repository.toggleFavorite(characterId, isFavorite) }
    }
}
