package com.cristianwer.pepinillorick.data.repository

import androidx.room.withTransaction
import com.cristianwer.pepinillorick.data.local.dao.CharacterDao
import com.cristianwer.pepinillorick.data.local.dao.CharacterWithFavoriteEntity
import com.cristianwer.pepinillorick.data.local.dao.FavoriteDao
import com.cristianwer.pepinillorick.data.local.dao.RemoteKeysDao
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import com.cristianwer.pepinillorick.domain.model.Resource
import com.cristianwer.pepinillorick.domain.model.UiError
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
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
    fun `getCharacters should return characters with favorite status from dao`() = runTest {
        // Given
        val characterId = 1
        val entities = listOf(
            CharacterWithFavoriteEntity(
                character = CharacterEntity(characterId, "Rick", "Alive", "Human", "", "Male", "", "", "", "", "", ""),
                isFavorite = true
            )
        )
        every { characterDao.getCharactersWithFavoriteFlow() } returns flowOf(entities)

        // When
        val result = repository.getCharacters().first()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isFavorite)
    }

    @Test
    fun `getCharactersWithSync should emit loading then success`() = runTest {
        // Given
        every { characterDao.getCharactersWithFavoriteFlow() } returns flowOf(emptyList())
        val response = CharacterResponseDto(
            info = InfoDto(20, 2, "next", null),
            results = emptyList()
        )
        coEvery { apiService.getCharacters(any()) } returns response

        // When
        val result = repository.getCharactersWithSync().take(2).toList()

        // Then
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Success)
    }

    @Test
    fun `getCharactersWithSync should emit connection error on IOException`() = runTest {
        // Given
        every { characterDao.getCharactersWithFavoriteFlow() } returns flowOf(emptyList())
        coEvery { apiService.getCharacters(any()) } throws IOException()

        // When
        val result = repository.getCharactersWithSync().take(2).toList()

        // Then
        assertTrue(result[0] is Resource.Loading)
        assertTrue(result[1] is Resource.Error)
        assertEquals(UiError.Connection, (result[1] as Resource.Error).uiError)
    }

    @Test
    fun `getFavoriteCharacters should return characters from dao marked as favorite`() = runTest {
        // Given
        val characterId = 1
        val entities = listOf(
            CharacterWithFavoriteEntity(
                character = CharacterEntity(characterId, "Rick", "Alive", "Human", "", "Male", "", "", "", "", "", ""),
                isFavorite = true
            )
        )
        every { characterDao.getFavoriteCharactersFlow() } returns flowOf(entities)

        // When
        val result = repository.getFavoriteCharacters().first()

        // Then
        assertEquals(1, result.size)
        assertTrue(result[0].isFavorite)
    }

    @Test
    fun `getCharacterById should return character with favorite status from dao`() = runTest {
        // Given
        val characterId = 1
        val entity = CharacterWithFavoriteEntity(
            character = CharacterEntity(characterId, "Rick", "Alive", "Human", "", "Male", "", "", "", "", "", ""),
            isFavorite = true
        )
        coEvery { characterDao.getCharacterWithFavoriteById(characterId) } returns entity

        // When
        val result = repository.getCharacterById(characterId)

        // Then
        assertEquals("Rick", result?.name)
        assertTrue(result?.isFavorite == true)
    }

    @Test
    fun `getCharacterByIdFlow should emit character with favorite status from dao`() = runTest {
        // Given
        val characterId = 1
        val entity = CharacterWithFavoriteEntity(
            character = CharacterEntity(characterId, "Rick", "Alive", "Human", "", "Male", "", "", "", "", "", ""),
            isFavorite = true
        )
        every { characterDao.getCharacterWithFavoriteByIdFlow(characterId) } returns flowOf(entity)

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
