package com.cristianwer.pepinillorick.domain.usecase

import androidx.paging.PagingData
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to retrieve the list of Rick & Morty characters.
 *
 * This use case acts as an interactor between the UI layer and the domain repository,
 * providing a paginated stream of character data.
 *
 * @property repository The repository used to fetch character data.
 */
internal class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    /**
     * Executes the use case to get a flow of paginated characters.
     *
     * @return A [Flow] of [PagingData] containing domain [Character] models.
     */
    operator fun invoke(): Flow<PagingData<Character>> {
        return repository.getCharacters()
    }
}
