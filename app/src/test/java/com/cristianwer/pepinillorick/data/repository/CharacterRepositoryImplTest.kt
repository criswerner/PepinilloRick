package com.cristianwer.pepinillorick.data.repository

import androidx.room.withTransaction
import com.cristianwer.pepinillorick.data.local.dao.CharacterDao
import com.cristianwer.pepinillorick.data.local.dao.RemoteKeysDao
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.platform.data.remote.dto.CharacterResponseDto
import com.cristianwer.platform.data.remote.dto.InfoDto
import com.cristianwer.platform.network.service.RickAndMortyApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
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
    private val remoteKeysDao: RemoteKeysDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> Any?>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        every { database.characterDao } returns characterDao
        every { database.remoteKeysDao } returns remoteKeysDao
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
    }

    @Test
    fun `syncCharacters should fetch page 1 when forceRefresh is true`() = runTest {
        // Given
        val response = CharacterResponseDto(
            info = InfoDto(20, 2, "next", null),
            results = emptyList()
        )
        coEvery { apiService.getCharacters(1) } returns response

        // When
        val result = repository.syncCharacters(forceRefresh = true)

        // Then
        assertTrue(result.isSuccess)
        coVerify { 
            apiService.getCharacters(1)
            characterDao.deleteAllCharacters()
            remoteKeysDao.deleteKey(any())
            characterDao.insertCharacters(any())
            remoteKeysDao.insertKey(any())
        }
    }

    @Test
    fun `syncCharacters should return failure when api throws exception`() = runTest {
        // Given
        coEvery { apiService.getCharacters(any()) } throws IOException()

        // When
        val result = repository.syncCharacters()

        // Then
        assertTrue(result.isFailure)
    }
}
