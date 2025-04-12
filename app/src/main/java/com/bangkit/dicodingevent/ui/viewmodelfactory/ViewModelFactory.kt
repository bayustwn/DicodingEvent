package com.bangkit.dicodingevent.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.dicodingevent.data.repository.EventRepository
import com.bangkit.dicodingevent.ui.viewmodel.EventViewModel
import com.bangkit.dicodingevent.utils.Injection

@Suppress("UNCHECKED_CAST")
class ViewModelFactory private constructor(private val eventRepository: EventRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventViewModel::class.java)){
            return EventViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown Class : " + modelClass.name)
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory{
            return INSTANCE ?: synchronized(this){

                    val instance = ViewModelFactory(Injection.eventRepo())
                    INSTANCE = instance
                    instance

            }
        }
    }

}