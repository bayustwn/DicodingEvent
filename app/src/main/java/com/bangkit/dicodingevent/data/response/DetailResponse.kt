package com.bangkit.dicodingevent.data.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

    @field:SerializedName("event")
    val listEvents: ListEventsItem,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)
