package com.example.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipe.databinding.PopularItemsBinding
import com.example.foodrecipe.pojo.MealsByCategory

class PopularMealAdapter(): RecyclerView.Adapter<PopularMealAdapter.PopularMealViewHolder>() {
    lateinit var onItemClick: ((MealsByCategory) -> Unit)
    private var mealList = ArrayList<MealsByCategory>()

    fun setMeals(mealList: ArrayList<MealsByCategory>){
        this.mealList = mealList
        notifyDataSetChanged()
    }

    class PopularMealViewHolder(val binding: PopularItemsBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false ))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealList[position].strMealThumb)
            .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealList[position])
        }
    }
}