package com.example.dynamicuiproject_xmlbase.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAllItems(): List<Item>

    @Insert
    suspend fun insertAll(items: List<Item>)
}