package com.cristianwer.pepinillorick.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity to store character IDs marked as favorite by the user.
 *
 * @property characterId The unique identifier of the favorite character.
 */
@Entity(tableName = "favorites")
internal data class FavoriteEntity(
    @PrimaryKey val characterId: Int
)
