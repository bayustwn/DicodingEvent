package com.bangkit.dicodingevent.data.api

import com.bangkit.dicodingevent.data.response.DetailResponse
import com.bangkit.dicodingevent.data.response.EventResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("events?")
    suspend fun getEvents(@QueryMap query: Map<String,Int>): EventResponse

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id:Int): DetailResponse

    @GET("events?")
    suspend fun searchEvent(@QueryMap query: Map<String,String> ): EventResponse

    @GET("events?active=-1&limit=1")
    suspend fun getLastest(): EventResponse
}