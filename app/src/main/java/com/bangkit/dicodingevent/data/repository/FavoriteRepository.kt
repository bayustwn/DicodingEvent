package com.bangkit.dicodingevent.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.bangkit.dicodingevent.data.db.dao.EventDao
import com.bangkit.dicodingevent.data.db.database.EventDatabase
import com.bangkit.dicodingevent.data.db.entity.EventEntity
import com.bangkit.dicodingevent.utils.AppExecutors

class FavoriteRepository private constructor(application: Application, private val appExecutors: AppExecutors) {

    private val eventDao: EventDao

    init {
        val db = EventDatabase.getDatabase(application)
        eventDao = db.eventDao()
    }

    fun getAllFav(): LiveData<List<EventEntity>> = eventDao.getAllFav()

    fun setFavorite(event: EventEntity){
        appExecutors.diskIO.execute { eventDao.setFavorite(event) }
    }

    fun unFavorite(id: Int){
        appExecutors.diskIO.execute { eventDao.deleteFavorite(id) }
    }

    fun isFavorite(id: Int): LiveData<Boolean>{
        return eventDao.isFavorite(id)
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteRepository? = null

        fun getInstance(application: Application,appExecutors: AppExecutors): FavoriteRepository{
            return INSTANCE ?: synchronized(this){
                val instance = FavoriteRepository(application,appExecutors)
                INSTANCE = instance
                instance
            }
        }
    }

}