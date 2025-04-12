package com.bangkit.dicodingevent.utils

import android.app.Application
import com.bangkit.dicodingevent.data.api.ApiClient
import com.bangkit.dicodingevent.data.repository.EventRepository
import com.bangkit.dicodingevent.data.repository.FavoriteRepository
import com.bangkit.dicodingevent.data.repository.SearchRepository

object Injection {
    fun eventRepo(): EventRepository{
        val apiService = ApiClient.apiService
        return EventRepository.getInstance(apiService)
    }

    fun searchRepo(): SearchRepository{
        val apiService = ApiClient.apiService
        return SearchRepository.getInstance(apiService)
    }

    fun favRepo(application: Application): FavoriteRepository{
        val appExecutors = AppExecutors()
        return FavoriteRepository.getInstance(application,appExecutors)
    }
}