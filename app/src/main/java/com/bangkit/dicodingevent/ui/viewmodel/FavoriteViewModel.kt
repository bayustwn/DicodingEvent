package com.bangkit.dicodingevent.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkit.dicodingevent.data.db.entity.EventEntity
import com.bangkit.dicodingevent.data.repository.FavoriteRepository

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository): ViewModel() {

    fun getAllFav(): LiveData<List<EventEntity>> = favoriteRepository.getAllFav()

    fun setFav(event: EventEntity){
        favoriteRepository.setFavorite(event)
    }

    fun unFav(id: Int){
        favoriteRepository.unFavorite(id)
    }

    fun isFav(id: Int):LiveData<Boolean>{
       return favoriteRepository.isFavorite(id)
    }

    fun toggleFav(event: EventEntity,fav:Boolean){
        if (fav){
            unFav(event.id)
        }else{
            setFav(event)
        }
    }


}