package com.cristianwer.pepinillorick.data.repository

import androidx.room.withTransaction
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.pepinillorick.data.local.entity.RemoteKeysEntity
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
 * This implementation manages synchronization and local storage manually,
 * keeping track of pagination state via a remote keys table.
 */
internal class CharacterRepositoryImpl @Inject constructor(
    private val apiService: RickAndMortyApiService,
    private val database: RickAndMortyDatabase
) : CharacterRepository {

    private companion object {
        const val CHARACTER_REMOTE_KEY_LABEL = "characters"
    }

    override fun getCharacters(): Flow<List<Character>> {
        return database.characterDao.getCharactersFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCharacterById(id: Int): Character? {
        return database.characterDao.getCharacterById(id)?.toDomain()
    }

    override suspend fun syncCharacters(forceRefresh: Boolean): Result<Unit> {
        return try {
            val pageToLoad = if (forceRefresh) {
                1
            } else {
                database.remoteKeysDao.getRemoteKey(CHARACTER_REMOTE_KEY_LABEL)?.nextPage ?: 1
            }

            val response = apiService.getCharacters(pageToLoad)
            val entities = response.results.map { it.toEntity() }
            val isLastPage = response.info.next == null
            val nextPage = if (isLastPage) pageToLoad else pageToLoad + 1

            database.withTransaction {
                if (forceRefresh) {
                    database.characterDao.deleteAllCharacters()
                    database.remoteKeysDao.deleteKey(CHARACTER_REMOTE_KEY_LABEL)
                }
                
                database.characterDao.insertCharacters(entities)
                database.remoteKeysDao.insertKey(
                    RemoteKeysEntity(CHARACTER_REMOTE_KEY_LABEL, nextPage)
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
