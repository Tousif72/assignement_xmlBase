package com.example.dynamicuiproject_xmlbase.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.dynamicuiproject_xmlbase.data.AppDatabase
import com.example.dynamicuiproject_xmlbase.data.Item
import com.example.dynamicuiproject_xmlbase.repository.ItemRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository
    val itemsLiveData = MutableLiveData<List<Item>>()

    private var allItems: List<Item> = emptyList()

    init {
        val db = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "item-database"
        ).build()
        repository = ItemRepository(db.itemDao())
        preloadDummyData()
    }

    private fun preloadDummyData() {
        viewModelScope.launch {
            val dummyItems = (1..15).map {
                Item(
                    title = "Title $it",
                    subtitle = "Subtitle $it",
                    imageUrl = "https://dummyimage.com/600x400/${100 * it}/fff"
                )
            }
            repository.insertAll(dummyItems)
            allItems = repository.getAllItems()
            itemsLiveData.postValue(allItems)
        }
    }

    fun filterItemsBySlider(position: Int) {
        val filteredItems = allItems.filter { it.title.contains("Title ${position + 1}") }
        itemsLiveData.postValue(filteredItems)
    }

    fun searchItems(query: String?) {
        if (query.isNullOrEmpty()) {
            itemsLiveData.postValue(allItems)
        } else {
            val filteredItems = allItems.filter {
                it.title.contains(query, ignoreCase = true) || it.subtitle.contains(query, ignoreCase = true)
            }
            itemsLiveData.postValue(filteredItems)
        }
    }
}
