package com.cristianwer.pepinillorick.data.repository

import androidx.room.withTransaction
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.pepinillorick.data.local.entity.FavoriteEntity
import com.cristianwer.pepinillorick.data.local.entity.RemoteKeysEntity
import com.cristianwer.pepinillorick.data.mapper.toDomain
import com.cristianwer.pepinillorick.data.mapper.toEntity
import com.cristianwer.pepinillorick.data.remote.RickAndMortyApiService
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.model.Resource
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

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
        return database.characterDao.getCharactersWithFavoriteFlow().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getCharactersWithSync(forceRefresh: Boolean): Flow<Resource<List<Character>>> = flow {
        val localDbFlow = getCharacters()
        val cachedData = localDbFlow.first()

        emit(Resource.Loading(cachedData))

        try {
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
            emitAll(localDbFlow.map { Resource.Success(it) })
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            emitAll(localDbFlow.map { Resource.Error(e.message ?: "Unknown error", it) })
        }
    }

    override fun getFavoriteCharacters(): Flow<List<Character>> {
        return database.characterDao.getFavoriteCharactersFlow().map { entities ->
            entities.map { it.toDomain().copy(isFavorite = true) }
        }
    }

    override suspend fun getCharacterById(id: Int): Character? {
        return database.characterDao.getCharacterWithFavoriteById(id)?.toDomain()
    }

    override fun getCharacterByIdFlow(id: Int): Flow<Character?> {
        return database.characterDao.getCharacterWithFavoriteByIdFlow(id).map { entity ->
            entity?.toDomain()
        }
    }

    override suspend fun toggleFavorite(id: Int, isFavorite: Boolean) {
        if (isFavorite) {
            database.favoriteDao.insertFavorite(FavoriteEntity(id))
        } else {
            database.favoriteDao.deleteFavorite(FavoriteEntity(id))
        }
    }
}
