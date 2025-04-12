package com.bangkit.dicodingevent.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateFormat {

    fun formatDate(data:String): String{
        val inputDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val date = inputDate.parse(data)!!
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val formatedDate = dateFormat.format(date)
        return formatedDate
    }

    fun formatTime(data:String): String{
        val inputDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val time = inputDate.parse(data)!!
        val timeFormat = SimpleDateFormat("HH:mm", Locale.US)
        val formatedTime = timeFormat.format(time)
        return formatedTime
    }

}