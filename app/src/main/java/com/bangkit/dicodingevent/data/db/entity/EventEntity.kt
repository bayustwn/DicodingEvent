package com.bangkit.dicodingevent.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class EventEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val mediaCover: String,
    val category: String,
    val cityName: String,
    val quota: Int,
    val registrants: Int,
    val beginTime: String,
    val isUpcoming:Boolean = false
)
