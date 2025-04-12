package com.bangkit.dicodingevent.data.repository

import androidx.lifecycle.MutableLiveData
import com.bangkit.dicodingevent.data.api.ApiService
import com.bangkit.dicodingevent.data.response.EventResponse
import com.bangkit.dicodingevent.utils.ResponseState

class SearchRepository private constructor(private val apiService: ApiService){

    val searchResult = MutableLiveData<ResponseState<EventResponse>>()

    suspend fun search(search: String, upcoming: Boolean){
        searchResult.postValue(ResponseState.Loading)
        try {
            val query = mutableMapOf<String,String>()
            query["q"] = search
            query["active"] = if (upcoming) "1" else "0"
            val response = apiService.searchEvent(query)
            searchResult.postValue(ResponseState.Success(response))
        }catch (e : Exception){
            searchResult.postValue(ResponseState.Error("Error"))
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: SearchRepository? = null

        fun getInstance(apiService: ApiService): SearchRepository{
            return INSTANCE ?: synchronized(this){
                val instance = SearchRepository(apiService)
                INSTANCE = instance
                instance
            }
        }
    }

}