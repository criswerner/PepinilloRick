package com.cristianwer.pepinillorick.domain.repository

import com.cristianwer.pepinillorick.domain.model.Character
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
     * Retrieves a specific character by their unique identifier.
     *
     * @param id The unique identifier of the character.
     * @return The character if found, null otherwise.
     */
    suspend fun getCharacterById(id: Int): Character?

    /**
     * Synchronizes a specific page of characters from the network to the local database.
     *
     * @param page The page number to fetch.
     * @return Result indicating success or failure.
     */
    suspend fun syncCharacters(page: Int): Result<Unit>
}
