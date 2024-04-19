package com.example.foodcart.ui.adapter
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcart.databinding.FoodItemBinding
import com.example.foodcart.model.FoodItem
import com.example.foodcart.ui.viewmodel.FoodCartViewModel


class FoodItemAdapter(private val viewModel: FoodCartViewModel) :
    RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>() {

    private var recentlyDeletedItemPosition: Int = -1
    private var recentlyDeletedItem: FoodItem? = null
    private var deleteFlag: Boolean = false
    private var handler: Handler? = null

    inner class FoodItemViewHolder(private val binding: FoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteButton.setOnClickListener {
                recentlyDeletedItemPosition = adapterPosition
                recentlyDeletedItem = viewModel.foodItems.value?.get(adapterPosition)
                deleteFlag = true

                // Show undo button and hide linearlayout for 10 seconds
                binding.undoButton.visibility = android.view.View.VISIBLE
                binding.linearlayout.visibility = android.view.View.GONE

                val currentPosition = adapterPosition // Capture the current position

                handler = Handler().apply {
                    postDelayed({
                        if (deleteFlag && recentlyDeletedItemPosition == currentPosition) {
                            // If the delete flag is still true and the position hasn't changed, perform deletion
                            viewModel.deleteItem(recentlyDeletedItemPosition)
                            binding.undoButton.visibility = android.view.View.GONE
                            deleteFlag = false
                        }
                    }, UNDO_TIMEOUT)
                }
            }

            binding.undoButton.setOnClickListener {
                // Remove pending callbacks from the Handler
                handler?.removeCallbacksAndMessages(null)

                // Restore visibility of linearlayout and hide undo button
                binding.undoButton.visibility = android.view.View.GONE
                binding.linearlayout.visibility = android.view.View.VISIBLE
                deleteFlag = false

                // Undo delete action
                recentlyDeletedItem?.let {
                    viewModel.undoDelete()
                }
            }
        }

        fun bind(foodItem: FoodItem) {
            binding.itemNameTextView.text = foodItem.name
            binding.itemPriceTextView.text = "${foodItem.price}"
            binding.itemCountTextView.text = "${foodItem.quantity}"

            binding.addButton.setOnClickListener {
                viewModel.addItemQuantity(adapterPosition)
            }

            binding.subtractButton.setOnClickListener {
                viewModel.subtractItemQuantity(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FoodItemBinding.inflate(layoutInflater, parent, false)
        return FoodItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val foodItem = viewModel.foodItems.value!![position]
        holder.bind(foodItem)
    }

    override fun getItemCount(): Int {
        return viewModel.foodItems.value?.size ?: 0
    }

    companion object {
        private const val UNDO_TIMEOUT = 10000L // 10 seconds
    }
}
