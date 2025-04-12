package com.bangkit.dicodingevent.data.repository

import androidx.lifecycle.MutableLiveData
import com.bangkit.dicodingevent.data.api.ApiService
import com.bangkit.dicodingevent.data.response.DetailResponse
import com.bangkit.dicodingevent.data.response.EventResponse
import com.bangkit.dicodingevent.utils.ResponseState

class EventRepository private constructor(private val apiService: ApiService) {

    val upcoming = MutableLiveData<ResponseState<EventResponse>>()
    val finished = MutableLiveData<ResponseState<EventResponse>>()
    val detail = MutableLiveData<ResponseState<DetailResponse>>()

    suspend fun getUpcoming(limit: Int?){
        upcoming.postValue(ResponseState.Loading)
        try {
            val queryMap = mutableMapOf<String,Int>()
            queryMap["active"] = 1
            limit?.apply { queryMap["limit"] = limit }
            val response = apiService.getEvents(queryMap)
            upcoming.postValue(ResponseState.Success(response))
        }catch (e: Exception){
            upcoming.postValue(ResponseState.Error("Error"))
        }
    }

    suspend fun getFinished(limit: Int?){
        finished.postValue(ResponseState.Loading)
        try {
            val queryMap = mutableMapOf<String,Int>()
            queryMap["active"] = 0
            limit?.apply { queryMap["limit"] = limit }
            val response = apiService.getEvents(queryMap)
            finished.postValue(ResponseState.Success(response))
        }catch (e: Exception){
            finished.postValue(ResponseState.Error("Error"))
        }
    }

    suspend fun getDetail(id:Int){
        detail.postValue(ResponseState.Loading)
        try {
            val response = apiService.getEventDetail(id)
            detail.postValue(ResponseState.Success(response))
        }catch (e: Exception){
            detail.postValue(ResponseState.Error("Error"))
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(apiService: ApiService): EventRepository{
            return INSTANCE ?: synchronized(this){
                val instance = EventRepository(apiService)
                INSTANCE = instance
                instance
            }
        }
    }


}