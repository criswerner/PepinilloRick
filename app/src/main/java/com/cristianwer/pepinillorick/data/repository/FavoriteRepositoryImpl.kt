package com.cristianwer.pepinillorick.data.repository

import com.cristianwer.pepinillorick.data.local.dao.FavoriteDao
import com.cristianwer.pepinillorick.data.local.entity.FavoriteEntity
import com.cristianwer.pepinillorick.domain.model.FavoriteType
import com.cristianwer.pepinillorick.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [FavoriteRepository] that handles multiple entity types.
 */
internal class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override suspend fun toggleFavorite(id: Int, type: FavoriteType, isFavorite: Boolean) {
        val entity = FavoriteEntity(id, type.name)
        if (isFavorite) {
            favoriteDao.insertFavorite(entity)
        } else {
            favoriteDao.deleteFavorite(entity)
        }
    }

    override fun getFavoriteIds(type: FavoriteType): Flow<List<Int>> {
        return favoriteDao.getFavoriteIdsByTypeFlow(type.name)
    }

    override fun isFavorite(id: Int, type: FavoriteType): Flow<Boolean> {
        return favoriteDao.isFavoriteFlow(id, type.name)
    }
}
