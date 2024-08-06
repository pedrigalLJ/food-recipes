package com.example.foodrecipe.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodrecipe.pojo.CategoryList
import com.example.foodrecipe.pojo.CategoryMeals
import com.example.foodrecipe.pojo.Meal
import com.example.foodrecipe.pojo.MealList
import com.example.foodrecipe.retrofit.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(): ViewModel() {

    private val _randomMealData =  MutableStateFlow<Meal?>(null)
    val randomMealData: StateFlow<Meal?> = _randomMealData.asStateFlow()

    private val _popularMeal = MutableStateFlow<List<CategoryMeals>?>(null)
    val popularMeal: StateFlow<List<CategoryMeals>?> = _popularMeal.asStateFlow()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal = response.body()!!.meals[0]
                    _randomMealData.value = randomMeal
                } else return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })

    }


    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.body() != null) {
                    _popularMeal.value = response.body()!!.meals
                } else return
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }
}