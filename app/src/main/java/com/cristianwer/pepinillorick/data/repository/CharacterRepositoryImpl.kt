package com.cristianwer.pepinillorick.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.cristianwer.pepinillorick.data.local.database.RickAndMortyDatabase
import com.cristianwer.pepinillorick.data.mapper.toDomain
import com.cristianwer.pepinillorick.data.remote.paging.CharacterRemoteMediator
import com.cristianwer.pepinillorick.domain.model.Character
import com.cristianwer.pepinillorick.domain.repository.CharacterRepository
import com.cristianwer.platform.network.service.RickAndMortyApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class CharacterRepositoryImpl @Inject constructor(
    private val apiService: RickAndMortyApiService,
    private val database: RickAndMortyDatabase
) : CharacterRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 2,
                enablePlaceholders = false
            ),
            remoteMediator = CharacterRemoteMediator(
                apiService = apiService,
                database = database
            ),
            pagingSourceFactory = {
                database.characterDao.getAllCharacters()
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}
