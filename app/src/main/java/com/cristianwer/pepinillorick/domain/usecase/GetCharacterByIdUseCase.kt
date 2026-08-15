package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import javax.inject.Inject

/**
 * Use case to retrieve a single Rick & Morty character by its ID.
 *
 * @property repository The repository used to fetch character data.
 */
internal class GetCharacterByIdUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the use case to get a character by its identifier.
     *
     * @param id The unique identifier of the character.
     * @return The character if found, null otherwise.
     */
    suspend operator fun invoke(id: Int): Character? {
        return repository.getCharacterById(id)
    }
}
