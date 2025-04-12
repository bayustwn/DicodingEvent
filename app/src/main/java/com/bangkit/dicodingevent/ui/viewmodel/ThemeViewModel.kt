package com.bangkit.dicodingevent.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.dicodingevent.data.datastore.ThemePreferences
import kotlinx.coroutines.launch

class ThemeViewModel (private val themePref: ThemePreferences): ViewModel(){

    fun getTheme(): LiveData<Boolean>{
        return themePref.getTheme().asLiveData()
    }

    fun setTheme(isDark: Boolean){
        viewModelScope.launch {
            themePref.setTheme(isDark)
        }
    }
}