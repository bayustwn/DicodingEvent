package com.bangkit.dicodingevent.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.dicodingevent.data.datastore.ThemePreferences
import com.bangkit.dicodingevent.ui.viewmodel.ThemeViewModel

@Suppress("UNCHECKED_CAST")
class ThemeViewModelFactory private constructor(private val themePreferences: ThemePreferences): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ThemeViewModel::class.java)){
            return ThemeViewModel(themePreferences) as T
        }
        throw IllegalArgumentException("Unknown class : " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ThemeViewModelFactory? = null

        fun getInstance(themePreferences: ThemePreferences): ThemeViewModelFactory {
            return INSTANCE ?: synchronized(this){
                val instance = ThemeViewModelFactory(themePreferences)
                INSTANCE = instance
                instance
            }
        }
    }

}