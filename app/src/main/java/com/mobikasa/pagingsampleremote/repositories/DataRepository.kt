package com.mobikasa.pagingsampleremote.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mobikasa.pagingsampleremote.api.APIService
import com.mobikasa.pagingsampleremote.db.MyDatabase
import com.mobikasa.pagingsampleremote.models.Results
import kotlinx.coroutines.flow.Flow

class DataRepository(private val mDataBase: MyDatabase, private val mAPIService: APIService) {

    @ExperimentalPagingApi
     fun getData(): Flow<PagingData<Results>> {
        val pagingSourceFactory = { mDataBase.getResultDao().getAllData() }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = DataSource(mAPIService, mDataBase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}