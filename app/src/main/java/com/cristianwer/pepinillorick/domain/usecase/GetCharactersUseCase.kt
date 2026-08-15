package com.cristianwer.pepinillorick.domain.usecase

import androidx.paging.PagingData
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<PagingData<Character>> {
        return repository.getCharacters()
    }
}
