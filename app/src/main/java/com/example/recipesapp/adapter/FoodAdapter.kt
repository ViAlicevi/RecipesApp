package com.example.recipesapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipesapp.R
import com.example.recipesapp.data.Food
import com.example.recipesapp.databinding.ItemSecretBinding


class FoodAdapter(
    private val listener: (Int) -> Unit
): RecyclerView.Adapter<FoodViewHolder>() {

    private var foods: ArrayList<Food> = arrayListOf()

    inner class MyDiffUtil(
        private val newList: ArrayList<Food>,
        private val oldList: ArrayList<Food>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].foodName == newList[newItemPosition].foodName
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList == newList
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {

        return FoodViewHolder.from(parent, listener)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        val imageItem = getItemId(position)
        holder.bind(foods[position])

    }

    override fun getItemCount() = foods.size

    fun setFoods(newList: ArrayList<Food>) {
        val diffUtil = MyDiffUtil(oldList = foods, newList = newList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        foods = newList
        diffResult.dispatchUpdatesTo(this)
    }

}

class FoodViewHolder private constructor(
    private val binding: ItemSecretBinding,
    private val listener: (Int) -> Unit
                    ): RecyclerView.ViewHolder(binding.root){

    companion object {
        fun from(parent: ViewGroup, listener: (Int) -> Unit): FoodViewHolder {
            val binding =
                ItemSecretBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return FoodViewHolder(binding, listener)
        }
    }

    fun bind(post: Food) {
        with(binding) {
            card.setOnClickListener {
                listener(absoluteAdapterPosition)
            }
            Glide.with(imgPostImage.context)
                .load(post.url)
                .placeholder(R.drawable.ic_replay)
                .into(imgPostImage)
            tvFoodName.text = post.foodName
        }
    }

}
