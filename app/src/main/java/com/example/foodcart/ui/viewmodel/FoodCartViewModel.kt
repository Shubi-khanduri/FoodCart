package com.example.foodcart.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodcart.model.FoodItem

class FoodCartViewModel : ViewModel() {

    private val _foodItems = MutableLiveData<List<FoodItem>>()
    val foodItems: LiveData<List<FoodItem>> = _foodItems

    private val _totalItemCount = MutableLiveData<Int>()
    val totalItemCount: LiveData<Int> = _totalItemCount

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private val _undoButtonVisible = MutableLiveData<Boolean>()
    val undoButtonVisible: LiveData<Boolean> = _undoButtonVisible

    private var _deletedItem: FoodItem? = null

    init {
        // Initialize food items
        _foodItems.value = listOf(
            FoodItem("A", 29.5),
            FoodItem("B", 30.0),
            FoodItem("C", 50.0),
            FoodItem("D", 70.0)
        )
        _totalItemCount.value = 0
        _totalPrice.value = 0.0
        _undoButtonVisible.value = false
    }

    fun addItemQuantity(position: Int) {
        val currentList = _foodItems.value!!.toMutableList()
        currentList[position].quantity++
        _foodItems.value = currentList
        calculateTotal()
    }

    fun subtractItemQuantity(position: Int) {
        val currentList = _foodItems.value!!.toMutableList()
        if (currentList[position].quantity > 0) {
            currentList[position].quantity--
            _foodItems.value = currentList
            calculateTotal()
        }
    }

    private fun calculateTotal() {
        var totalCount = 0
        var totalPrice = 0.0
        _foodItems.value?.forEach {
            totalCount += it.quantity
            totalPrice += it.price * it.quantity
        }
        _totalItemCount.value = totalCount
        _totalPrice.value = totalPrice
    }

    fun deleteItem(position: Int) {
        _deletedItem = _foodItems.value?.get(position)
        val currentList = _foodItems.value!!.toMutableList()
        currentList.removeAt(position)
        _foodItems.value = currentList
        calculateTotal()
        _undoButtonVisible.value = true
    }

    fun undoDelete() {
        _deletedItem?.let {
            val currentList = _foodItems.value!!.toMutableList()
            currentList.add(_deletedItem!!)
            _foodItems.value = currentList
            _deletedItem = null
            calculateTotal()
            _undoButtonVisible.value = false
        }
    }

    fun hideUndoButton() {
        _undoButtonVisible.value = false
    }

    fun showUndoButton() {
        _undoButtonVisible.value = true
    }
}

