package com.cristianwer.pepinillorick.domain.repository

import androidx.paging.PagingData
import com.cristianwer.pepinillorick.domain.model.Character
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing the repository for accessing Rick & Morty characters.
 *
 * This repository abstracts the data source (local or remote) and provides
 * characters as a paginated stream.
 */
internal interface CharacterRepository {
    /**
     * Retrieves a paginated stream of characters.
     *
     * @return A [Flow] of [PagingData] containing [Character] objects.
     */
    fun getCharacters(): Flow<PagingData<Character>>
}
