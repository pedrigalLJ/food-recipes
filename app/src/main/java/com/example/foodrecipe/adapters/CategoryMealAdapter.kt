package com.example.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipe.databinding.CategoryItemBinding
import com.example.foodrecipe.pojo.Category
import com.example.foodrecipe.pojo.CategoryList
import com.example.foodrecipe.pojo.MealsByCategory

class CategoryMealAdapter(): RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>() {
    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    private var categoryList = ArrayList<Category>()

    fun setCategoryList(categoryList: List<Category>){
        this.categoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryMealViewHolder(val binding: CategoryItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(categoryList[position].strCategoryThumb)
            .into(holder.binding.imgCategoryMeal)

        holder.binding.tvCategoryMealName.text = categoryList[position].strCategory
    }
}