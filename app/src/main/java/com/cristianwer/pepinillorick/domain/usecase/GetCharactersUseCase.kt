package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
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
     * Executes the use case to get a flow of characters.
     */
    operator fun invoke(): Flow<List<Character>> {
        return repository.getCharacters()
    }
}
