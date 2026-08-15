package com.cristianwer.pepinillorick.data.repository

import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.pepinillorick.data.mapper.toDomain
import com.cristianwer.pepinillorick.data.mapper.toEntity
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import com.cristianwer.platform.network.service.RickAndMortyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of [CharacterRepository] using Room as a single source of truth.
 *
 * This implementation avoids experimental Paging APIs by managing synchronization
 * and local storage manually.
 */
internal class CharacterRepositoryImpl @Inject constructor(
    private val apiService: RickAndMortyApiService,
    private val database: RickAndMortyDatabase
) : CharacterRepository {

    override fun getCharacters(): Flow<List<Character>> {
        return database.characterDao.getCharactersFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCharacterById(id: Int): Character? {
        return database.characterDao.getCharacterById(id)?.toDomain()
    }

    override suspend fun syncCharacters(page: Int): Result<Unit> {
        return try {
            val response = apiService.getCharacters(page)
            val entities = response.results.map { it.toEntity() }
            
            if (page == 1) {
                database.characterDao.deleteAllCharacters()
            }
            database.characterDao.insertCharacters(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
