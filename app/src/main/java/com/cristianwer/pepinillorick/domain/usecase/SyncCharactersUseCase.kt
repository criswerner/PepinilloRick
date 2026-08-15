package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import javax.inject.Inject

/**
 * Use case to synchronize character data from the network.
 */
internal class SyncCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the synchronization for a specific page.
     *
     * @param page The page to sync.
     * @return Result of the operation.
     */
    suspend operator fun invoke(page: Int): Result<Unit> {
        return repository.syncCharacters(page)
    }
}
