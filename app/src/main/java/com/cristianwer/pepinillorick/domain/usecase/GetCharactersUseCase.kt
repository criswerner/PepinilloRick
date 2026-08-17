package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.Resource
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve the list of Rick & Morty characters from the local database.
 */
internal class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the use case to get a flow of characters with automatic synchronization.
     *
     * @param forceRefresh If true, starts from the first page and clears local data.
     */
    operator fun invoke(forceRefresh: Boolean = false): Flow<Resource<List<Character>>> {
        return repository.getCharactersWithSync(forceRefresh)
    }
}
