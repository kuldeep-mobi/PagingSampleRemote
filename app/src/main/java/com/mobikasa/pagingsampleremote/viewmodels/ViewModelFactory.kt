@file:Suppress("UNCHECKED_CAST")

package com.mobikasa.pagingsampleremote.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobikasa.pagingsampleremote.repositories.DataRepository

class ViewModelFactory(private val mRepository: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MyDataViewModel(mRepository = mRepository) as T
    }
}