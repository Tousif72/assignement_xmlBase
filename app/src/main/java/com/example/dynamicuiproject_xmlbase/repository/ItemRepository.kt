package com.example.dynamicuiproject_xmlbase.repository

import com.example.dynamicuiproject_xmlbase.data.Item
import com.example.dynamicuiproject_xmlbase.data.ItemDao


class ItemRepository(private val itemDao: ItemDao) {
    suspend fun getAllItems() = itemDao.getAllItems()
    suspend fun insertAll(items: List<Item>) = itemDao.insertAll(items)
}