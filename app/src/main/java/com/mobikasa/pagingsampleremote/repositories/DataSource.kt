package com.mobikasa.pagingsampleremote.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mobikasa.pagingsampleremote.api.APIService
import com.mobikasa.pagingsampleremote.db.MyDatabase
import com.mobikasa.pagingsampleremote.models.RemoteKey
import com.mobikasa.pagingsampleremote.models.Results
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@ExperimentalPagingApi
class DataSource(
    private val mAPIService: APIService,
    private val myDatabase: MyDatabase
) : RemoteMediator<Int, Results>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(30, TimeUnit.MINUTES)

        if (myDatabase.getRemoteDao().lastUpdated() != null) {
            return if (System.currentTimeMillis() - myDatabase.getRemoteDao()
                    .lastUpdated()!! <= cacheTimeout
            ) {
                // Cached data is up-to-date, so there is no need to re-fetch
                // from the network.
                InitializeAction.SKIP_INITIAL_REFRESH

            } else {
                // Need to refresh cached data from network; returning
                // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
                // APPEND and PREPEND from running until REFRESH succeeds.
                InitializeAction.LAUNCH_INITIAL_REFRESH
            }
        } else {
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Results>
    ): MediatorResult {
        val pageKeyData = getKeyPageData(loadType, state)
        val page = when (pageKeyData) {
            is MediatorResult.Success -> {
                return pageKeyData
            }
            else -> {
                pageKeyData as Long
            }
        }
        return try {
            val response = mAPIService.getAllMovies(page)
            val isEndOfList = response.results.isEmpty()
            myDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    myDatabase.getResultDao().deleteAllData()
                    myDatabase.getRemoteDao().deleteAllData()
                }
                val prevKey = if (page == 1L) null else page - 1L
                val nextKey = if (isEndOfList) null else page + 1L
                val keys = response.results.map {
                    RemoteKey(
                        it.id.toString(),
                        prevKey = prevKey,
                        nextKey = nextKey,
                        updateAt = System.currentTimeMillis()
                    )
                }
                myDatabase.getRemoteDao().insertAllKey(keys)
                myDatabase.getResultDao().insertAll(response.results)
            }
            return MediatorResult.Success(endOfPaginationReached = isEndOfList)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getKeyPageData(loadType: LoadType, state: PagingState<Int, Results>): Any {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.APPEND -> {
                val remoteKeys = getLastRemoteKey(state)
                val nextKey = remoteKeys?.nextKey
                return nextKey ?: MediatorResult.Success(endOfPaginationReached = false)
            }
            LoadType.PREPEND -> {
                val remoteKeys = getFirstRemoteKey(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                prevKey
            }
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Results>): RemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                myDatabase.getRemoteDao().getRemoteKeyById(id.toString())
            }
        }
    }

    private suspend fun getLastRemoteKey(state: PagingState<Int, Results>): RemoteKey? {
        return state.pages
            .lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()
            ?.let { mData ->
                myDatabase.getRemoteDao().getRemoteKeyById(
                    mData.id.toString()
                )
            }
    }

    private suspend fun getFirstRemoteKey(state: PagingState<Int, Results>): RemoteKey? {
        return state.pages
            .firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()
            ?.let { mData -> myDatabase.getRemoteDao().getRemoteKeyById(mData.id.toString()) }
    }
}