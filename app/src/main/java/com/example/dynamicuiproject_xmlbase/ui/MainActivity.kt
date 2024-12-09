package com.example.dynamicuiproject_xmlbase.ui

import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dynamicuiproject_xmlbase.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        // Set up ViewPager
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val sliderAdapter = SliderAdapter(getDummySliderImages())
        viewPager.adapter = sliderAdapter

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter

        // Observe data
        viewModel.itemsLiveData.observe(this) { items ->
            recyclerViewAdapter.submitList(items)
        }

        // Listen to slider changes
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.filterItemsBySlider(position)
            }
        })

        // Set up SearchView
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchItems(newText)
                return true
            }
        })

        // Set up FAB
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    private fun getDummySliderImages(): List<String> = listOf(
        "https://dummyimage.com/600x400/000/fff",
        "https://dummyimage.com/600x400/111/eee",
        "https://dummyimage.com/600x400/222/ddd",
        "https://dummyimage.com/600x400/333/ccc",
        "https://dummyimage.com/600x400/444/bbb"
    )

    private fun showBottomSheetDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottomsheet_dialog, null)
        val itemCountTextView = view.findViewById<TextView>(R.id.itemCount)
        itemCountTextView.text = "Total items: ${recyclerViewAdapter.itemCount}"
        dialog.setContentView(view)
        dialog.show()
    }
}
