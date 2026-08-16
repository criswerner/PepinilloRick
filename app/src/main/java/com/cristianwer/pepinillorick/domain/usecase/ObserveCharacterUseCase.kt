package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to observe a specific Rick & Morty character as a stream.
 *
 * @property repository The repository used to fetch character data.
 */
internal class ObserveCharacterUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the use case to observe a character by its identifier.
     *
     * @param id The unique identifier of the character.
     * @return A [Flow] emitting the character if found, null otherwise.
     */
    operator fun invoke(id: Int): Flow<Character?> {
        return repository.getCharacterByIdFlow(id)
    }
}
