package com.bangkit.dicodingevent.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.dicodingevent.data.repository.SearchRepository
import com.bangkit.dicodingevent.ui.viewmodel.SearchViewModel
import com.bangkit.dicodingevent.utils.Injection

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory private constructor(private val searchRepository: SearchRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(searchRepository) as T
        }
        throw IllegalArgumentException("Unknown class : " + modelClass.name)
    }

    companion object{
        @Volatile
        private var INSTANCE : SearchViewModelFactory?=null

        fun getInstance(): SearchViewModelFactory{
            return INSTANCE?: synchronized(this){
                val instance = SearchViewModelFactory(Injection.searchRepo())
                INSTANCE = instance
                instance
            }
        }
    }

}