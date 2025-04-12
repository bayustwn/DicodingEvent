package com.bangkit.dicodingevent.ui.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.dicodingevent.data.repository.FavoriteRepository
import com.bangkit.dicodingevent.ui.viewmodel.FavoriteViewModel
import com.bangkit.dicodingevent.utils.Injection

@Suppress("UNCHECKED_CAST")
class FavoriteViewModelFactory private constructor(private val favoriteRepository: FavoriteRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(favoriteRepository) as T
        }
        throw IllegalArgumentException("Unknown class " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: FavoriteViewModelFactory? = null

        fun getInstance(application: Application): FavoriteViewModelFactory{
            return INSTANCE ?: synchronized(this){
                val instance = FavoriteViewModelFactory(Injection.favRepo(application))
                INSTANCE = instance
                instance
            }
        }
    }

}