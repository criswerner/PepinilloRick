package com.cristianwer.pepinillorick.data.local.entity

import androidx.room.Entity

/**
 * Entity to store IDs marked as favorite by the user across different entity types.
 *
 * @property id The unique identifier of the favorite item.
 * @property type The type of item (CHARACTER, EPISODE, etc.).
 */
@Entity(
    tableName = "favorites",
    primaryKeys = ["id", "type"]
)
internal data class FavoriteEntity(
    val id: Int,
    val type: String
)
