package com.cristianwer.pepinillorick.data.repository

import com.cristianwer.pepinillorick.data.local.dao.FavoriteDao
import com.cristianwer.pepinillorick.data.local.entity.FavoriteEntity
import com.cristianwer.pepinillorick.domain.model.FavoriteType
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [FavoriteRepositoryImpl].
 */
internal class FavoriteRepositoryImplTest {

    private lateinit var repository: FavoriteRepositoryImpl
    private val favoriteDao: FavoriteDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = FavoriteRepositoryImpl(favoriteDao)
    }

    @Test
    fun `toggleFavorite should insert in favoriteDao when isFavorite is true`() = runTest {
        // When
        repository.toggleFavorite(1, FavoriteType.CHARACTER, true)

        // Then
        coVerify { favoriteDao.insertFavorite(FavoriteEntity(1, "CHARACTER")) }
    }

    @Test
    fun `toggleFavorite should delete from favoriteDao when isFavorite is false`() = runTest {
        // When
        repository.toggleFavorite(1, FavoriteType.CHARACTER, false)

        // Then
        coVerify { favoriteDao.deleteFavorite(FavoriteEntity(1, "CHARACTER")) }
    }

    @Test
    fun `getFavoriteIds should return ids from dao for specific type`() = runTest {
        // Given
        val ids = listOf(1, 2, 3)
        every { favoriteDao.getFavoriteIdsByTypeFlow("CHARACTER") } returns flowOf(ids)

        // When
        val result = repository.getFavoriteIds(FavoriteType.CHARACTER).first()

        // Then
        assertEquals(ids, result)
    }

    @Test
    fun `isFavorite should return status from dao for specific type`() = runTest {
        // Given
        every { favoriteDao.isFavoriteFlow(1, "CHARACTER") } returns flowOf(true)

        // When
        val result = repository.isFavorite(1, FavoriteType.CHARACTER).first()

        // Then
        assertTrue(result)
    }
}
