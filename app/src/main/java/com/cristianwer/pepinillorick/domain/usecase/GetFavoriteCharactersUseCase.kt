package com.cristianwer.pepinillorick.domain.usecase

import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve only the Rick & Morty characters marked as favorites.
 *
 * @property repository The repository used to fetch character data.
 */
internal class GetFavoriteCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the use case to get a flow of favorite characters.
     *
     * @return A [Flow] of [List] containing favorite [Character] objects.
     */
    operator fun invoke(): Flow<List<Character>> {
        return repository.getFavoriteCharacters()
    }
}
