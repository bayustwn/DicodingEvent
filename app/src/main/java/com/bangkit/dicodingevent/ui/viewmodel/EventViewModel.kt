package com.bangkit.dicodingevent.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.dicodingevent.data.repository.EventRepository
import com.bangkit.dicodingevent.data.response.DetailResponse
import com.bangkit.dicodingevent.data.response.EventResponse
import com.bangkit.dicodingevent.utils.ResponseState
import kotlinx.coroutines.launch

class EventViewModel(private val eventRepository: EventRepository) : ViewModel() {

    val upcoming: LiveData<ResponseState<EventResponse>> = eventRepository.upcoming
    val finished: LiveData<ResponseState<EventResponse>> =  eventRepository.finished
    val detail: LiveData<ResponseState<DetailResponse>> = eventRepository.detail

    fun getFinished(limit: Int?) {
        viewModelScope.launch {
            eventRepository.getFinished(limit)
        }
    }

    fun getUpcoming(limit: Int?) {
        viewModelScope.launch {
            eventRepository.getUpcoming(limit)
        }
    }

    fun getDetail(id:Int){
        viewModelScope.launch {
            eventRepository.getDetail(id)
        }
    }

}