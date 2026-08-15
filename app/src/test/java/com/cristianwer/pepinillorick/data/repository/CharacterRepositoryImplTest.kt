package com.cristianwer.pepinillorick.data.repository

import com.cristianwer.pepinillorick.data.local.dao.CharacterDao
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.platform.data.remote.dto.CharacterResponseDto
import com.cristianwer.platform.data.remote.dto.InfoDto
import com.cristianwer.platform.network.service.RickAndMortyApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

/**
 * Unit tests for [CharacterRepositoryImpl].
 */
internal class CharacterRepositoryImplTest {

    private lateinit var repository: CharacterRepositoryImpl
    private val apiService: RickAndMortyApiService = mockk()
    private val database: RickAndMortyDatabase = mockk()
    private val characterDao: CharacterDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        every { database.characterDao } returns characterDao
        repository = CharacterRepositoryImpl(apiService, database)
    }

    @Test
    fun `getCharacters should return flow from dao mapped to domain`() = runTest {
        // Given
        every { characterDao.getCharactersFlow() } returns flowOf(emptyList())

        // When
        val result = repository.getCharacters().first()

        // Then
        assertTrue(result.isEmpty())
        verify { characterDao.getCharactersFlow() }
    }

    @Test
    fun `syncCharacters should fetch from api and insert into db`() = runTest {
        // Given
        val response = CharacterResponseDto(
            info = InfoDto(20, 2, null, null),
            results = emptyList()
        )
        coEvery { apiService.getCharacters(1) } returns response

        // When
        val result = repository.syncCharacters(1)

        // Then
        assertTrue(result.isSuccess)
        coVerify { 
            characterDao.deleteAllCharacters()
            characterDao.insertCharacters(any())
        }
    }

    @Test
    fun `syncCharacters should return failure when api throws exception`() = runTest {
        // Given
        coEvery { apiService.getCharacters(1) } throws IOException()

        // When
        val result = repository.syncCharacters(1)

        // Then
        assertTrue(result.isFailure)
    }
}
