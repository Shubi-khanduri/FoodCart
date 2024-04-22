package com.example.foodcart.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodcart.databinding.ActivityMainBinding
import com.example.foodcart.ui.adapter.FoodItemAdapter
import com.example.foodcart.ui.viewmodel.FoodCartViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: FoodCartViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        val adapter = FoodItemAdapter(viewModel)

        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = adapter

        viewModel.foodItems.observe(this) {
            Log.i("updatedList", it.toString())
            binding.recyclerView.adapter = adapter
        }

        viewModel.totalItemCount.observe(this) { count ->
            binding.totalItemCount.text = "Total Items: $count"
        }

        viewModel.totalPrice.observe(this) { price ->
            binding.totalPrice.text = "Total Price: $price"
        }

    }
}
