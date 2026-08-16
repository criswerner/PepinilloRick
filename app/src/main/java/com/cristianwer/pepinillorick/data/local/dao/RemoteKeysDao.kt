package com.cristianwer.pepinillorick.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cristianwer.pepinillorick.data.local.entity.RemoteKeysEntity

/**
 * Data access object for the remote keys table.
 */
@Dao
internal interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: RemoteKeysEntity)

    @Query("SELECT * FROM remote_keys WHERE label = :label")
    suspend fun getRemoteKey(label: String): RemoteKeysEntity?

    @Query("DELETE FROM remote_keys WHERE label = :label")
    suspend fun deleteKey(label: String)
}
