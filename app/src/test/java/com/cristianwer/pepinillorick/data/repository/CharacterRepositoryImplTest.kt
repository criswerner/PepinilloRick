package com.cristianwer.pepinillorick.data.repository

import androidx.room.withTransaction
import com.cristianwer.pepinillorick.data.local.dao.CharacterDao
import com.cristianwer.pepinillorick.data.local.dao.FavoriteDao
import com.cristianwer.pepinillorick.data.local.dao.RemoteKeysDao
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import com.cristianwer.pepinillorick.domain.model.Resource
import com.cristianwer.pepinillorick.data.remote.RickAndMortyApiService
import com.cristianwer.pepinillorick.data.remote.dto.CharacterResponseDto
import com.cristianwer.pepinillorick.data.remote.dto.InfoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
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
    private val favoriteDao: FavoriteDao = mockk(relaxed = true)
    private val remoteKeysDao: RemoteKeysDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic("androidx.room.RoomDatabaseKt")
        val transactionLambda = slot<suspend () -> Any?>()
        coEvery { database.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }

        every { database.characterDao } returns characterDao
        every { database.favoriteDao } returns favoriteDao
        every { database.remoteKeysDao } returns remoteKeysDao
        repository = CharacterRepositoryImpl(apiService, database)
    }

    @Test
    fun `getCharacters should combine characters from dao and favorite ids`() = runTest {
        // Given
        val characterId = 1
        val entities = listOf(
            CharacterEntity(characterId, "Rick", "Alive", "Human", "", "Male", "", "", "", "", "", "")
        )
        every { characterDao.getCharactersFlow() } returns flowOf(entities)
        every { favoriteDao.getAllFavoriteIdsFlow() } returns flowOf(listOf(characterId))

        // When
        val result = repository.getCharacters().first()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isFavorite)
    }

    @Test
    fun `getCharactersWithSync should emit loading then success`() = runTest {
        // Given
        every { characterDao.getCharactersFlow() } returns flowOf(emptyList())
        every { favoriteDao.getAllFavoriteIdsFlow() } returns flowOf(emptyList())
        val response = CharacterResponseDto(
            info = InfoDto(20, 2, "next", null),
            results = emptyList()
        )
        coEvery { apiService.getCharacters(any()) } returns response

        // When
        val emissions = mutableListOf<Resource<List<com.cristianwer.pepinillorick.domain.model.Character>>>()
        val job = launch {
            repository.getCharactersWithSync().collect { emissions.add(it) }
        }

        // Then
        assertTrue(emissions[0] is Resource.Loading)
        job.cancel()
    }

    @Test
    fun `getFavoriteCharacters should return characters from dao marked as favorite`() = runTest {
        // Given
        val characterId = 1
        val entities = listOf(
            CharacterEntity(characterId, "Rick", "Alive", "Human", "", "Male", "", "", "", "", "", "")
        )
        every { characterDao.getFavoriteCharactersFlow() } returns flowOf(entities)

        // When
        val result = repository.getFavoriteCharacters().first()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isFavorite)
    }

    @Test
    fun `getCharacterByIdFlow should emit character with favorite status`() = runTest {
        // Given
        val characterId = 1
        val entity = CharacterEntity(characterId, "Rick", "Alive", "Human", "", "Male", "", "", "", "", "", "")
        every { characterDao.getCharacterByIdFlow(characterId) } returns flowOf(entity)
        every { favoriteDao.isFavoriteFlow(characterId) } returns flowOf(true)

        // When
        val result = repository.getCharacterByIdFlow(characterId).first()

        // Then
        assertTrue(result?.isFavorite == true)
    }

    @Test
    fun `toggleFavorite should insert in favoriteDao when isFavorite is true`() = runTest {
        // When
        repository.toggleFavorite(1, true)

        // Then
        coVerify { favoriteDao.insertFavorite(any()) }
    }

    @Test
    fun `toggleFavorite should delete from favoriteDao when isFavorite is false`() = runTest {
        // When
        repository.toggleFavorite(1, false)

        // Then
        coVerify { favoriteDao.deleteFavorite(any()) }
    }
}
