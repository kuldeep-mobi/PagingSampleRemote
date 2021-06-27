package com.mobikasa.pagingsampleremote.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mobikasa.pagingsampleremote.models.Results
import com.mobikasa.pagingsampleremote.repositories.DataRepository
import kotlinx.coroutines.flow.Flow

class MyDataViewModel(private val mRepository: DataRepository) :
    ViewModel() {
    @ExperimentalPagingApi
    private val result = mRepository.getData().cachedIn(viewModelScope)

    @ExperimentalPagingApi
    fun getResults(): Flow<PagingData<Results>> {
        return result
    }
}