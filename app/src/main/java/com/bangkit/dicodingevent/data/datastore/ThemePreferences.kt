package com.bangkit.dicodingevent.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")

class ThemePreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_setting")

    fun getTheme(): Flow<Boolean> {
        return dataStore.data.map { prev->
            prev[themeKey] ?: false
        }
    }

    suspend fun setTheme(isDark: Boolean){
        dataStore.edit { prev->
            prev[themeKey] = isDark
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: ThemePreferences?=null

        fun getInstance(dataStore: DataStore<Preferences>):ThemePreferences{
            return INSTANCE ?: synchronized(this){
                val instance = ThemePreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}