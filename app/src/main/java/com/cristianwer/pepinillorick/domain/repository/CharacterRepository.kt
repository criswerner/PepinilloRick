package com.cristianwer.pepinillorick.domain.repository

import androidx.paging.PagingData
import com.cristianwer.pepinillorick.domain.model.Character
import kotlinx.coroutines.flow.Flow

internal interface CharacterRepository {
    fun getCharacters(): Flow<PagingData<Character>>
}
