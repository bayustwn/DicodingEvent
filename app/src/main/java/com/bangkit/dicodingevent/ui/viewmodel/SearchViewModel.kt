package com.bangkit.dicodingevent.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dicodingevent.data.repository.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val searchRepository: SearchRepository): ViewModel() {

    val search = searchRepository.searchResult

    fun searchEvent(query: String,upcoming:Boolean){
        viewModelScope.launch {
            searchRepository?.search(query,upcoming)
        }
    }

}