package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import javax.inject.Inject

/**
 * Use case to synchronize character data from the network.
 * 
 * It automatically handles pagination state via the repository.
 */
internal class SyncCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the synchronization.
     *
     * @param forceRefresh If true, starts from the first page and clears local data.
     * @return Result of the operation.
     */
    suspend operator fun invoke(forceRefresh: Boolean = false): Result<Unit> {
        return repository.syncCharacters(forceRefresh)
    }
}
