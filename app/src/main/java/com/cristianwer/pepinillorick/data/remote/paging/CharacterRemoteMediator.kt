package com.cristianwer.pepinillorick.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.pepinillorick.data.local.entity.CharacterEntity
import com.cristianwer.pepinillorick.data.local.entity.RemoteKeysEntity
import com.cristianwer.pepinillorick.data.mapper.toEntity
import com.cristianwer.platform.network.service.RickAndMortyApiService
import java.io.IOException

/**
 * RemoteMediator that handles pagination for the Rick & Morty characters by coordinating
 * between the remote API and the local Room database.
 *
 * It implements an offline-first strategy by fetching data from the network when needed
 * and persisting it in the local database, which serves as the single source of truth.
 *
 * @property apiService The RickAndMortyApiService used to fetch data from the network.
 * @property database The local Room database where characters and remote keys are stored.
 */
@OptIn(ExperimentalPagingApi::class)
internal class CharacterRemoteMediator(
    private val apiService: RickAndMortyApiService,
    private val database: RickAndMortyDatabase
) : RemoteMediator<Int, CharacterEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }

        try {
            val response = apiService.getCharacters(page)
            val characters = response.results
            val endOfPaginationReached = characters.isEmpty() || response.info.next == null

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao.clearRemoteKeys()
                    database.characterDao.deleteAllCharacters()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = characters.map {
                    RemoteKeysEntity(characterId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                database.remoteKeysDao.insertAll(keys)
                database.characterDao.insertCharacters(characters.map { it.toEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, CharacterEntity>): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { character ->
            database.remoteKeysDao.getRemoteKeysForCharacter(character.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, CharacterEntity>): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { character ->
            database.remoteKeysDao.getRemoteKeysForCharacter(character.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CharacterEntity>): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { characterId ->
                database.remoteKeysDao.getRemoteKeysForCharacter(characterId)
            }
        }
    }
}
