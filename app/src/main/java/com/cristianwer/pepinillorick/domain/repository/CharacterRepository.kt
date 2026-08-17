package com.cristianwer.pepinillorick.domain.repository

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing the repository for accessing Rick & Morty characters.
 */
internal interface CharacterRepository {
    /**
     * Retrieves a stream of characters from the local database.
     */
    fun getCharacters(): Flow<List<Character>>

    /**
     * Retrieves a stream of characters from the local database and synchronizes with the network.
     *
     * @param forceRefresh If true, it clears the local database and starts from page 1.
     * @return A [Flow] of [Resource] containing the characters and their status.
     */
    fun getCharactersWithSync(forceRefresh: Boolean = false): Flow<Resource<List<Character>>>

    /**
     * Retrieves a stream of characters marked as favorites.
     */
    fun getFavoriteCharacters(): Flow<List<Character>>

    /**
     * Retrieves a specific character by their unique identifier.
     *
     * @param id The unique identifier of the character.
     * @return The character if found, null otherwise.
     */
    suspend fun getCharacterById(id: Int): Character?

    /**
     * Retrieves a specific character as a stream by their unique identifier.
     *
     * @param id The unique identifier of the character.
     * @return A [Flow] emitting the character if found, null otherwise.
     */
    fun getCharacterByIdFlow(id: Int): Flow<Character?>

    /**
     * Toggles the favorite status of a character.
     *
     * @param id The unique identifier of the character.
     * @param isFavorite The new favorite status.
     */
    suspend fun toggleFavorite(id: Int, isFavorite: Boolean)
}
