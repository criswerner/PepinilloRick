package com.cristianwer.pepinillorick.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity to store the pagination keys for the character list.
 *
 * @property label A unique label for the pagination (e.g., "characters").
 * @property nextPage The next page number to fetch from the API.
 */
@Entity(tableName = "remote_keys")
internal data class RemoteKeysEntity(
    @PrimaryKey val label: String,
    val nextPage: Int
)
