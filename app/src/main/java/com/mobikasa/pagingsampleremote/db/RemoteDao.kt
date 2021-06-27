package com.mobikasa.pagingsampleremote.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobikasa.pagingsampleremote.models.RemoteKey

@Dao
interface RemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllKey(list: List<RemoteKey>)

    @Query("Select * from remote_keys Where id=:id")
    suspend fun getRemoteKeyById(id: String): RemoteKey?

    @Query("Delete from remote_keys")
    suspend fun deleteAllData()

    @Query("select updateAt from remote_keys ORDER BY  updateAt DESC  limit 1")
    suspend fun lastUpdated(): Long?
}