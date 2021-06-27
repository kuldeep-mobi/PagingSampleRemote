package com.mobikasa.pagingsampleremote.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobikasa.pagingsampleremote.models.Results

@Dao
interface ResultDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mList: List<Results>)

    @Query("Select * from result_data")
    fun getAllData(): PagingSource<Int, Results>

    @Query("Delete from result_data")
   suspend fun deleteAllData()
}