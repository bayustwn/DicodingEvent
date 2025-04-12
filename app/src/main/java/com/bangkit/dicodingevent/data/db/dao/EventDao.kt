package com.bangkit.dicodingevent.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.dicodingevent.data.db.entity.EventEntity

@Dao
interface EventDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setFavorite(event: EventEntity)

    @Query("SELECT * FROM event_table")
    fun getAllFav(): LiveData<List<EventEntity>>

    @Query("SELECT EXISTS(SELECT * FROM event_table WHERE id = :id LIMIT 1)")
    fun isFavorite(id: Int): LiveData<Boolean>

    @Query("DELETE FROM event_table WHERE id = :id")
    fun deleteFavorite(id:Int)

}