package com.example.foodrecipe.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.foodrecipe.pojo.Category
import com.example.foodrecipe.pojo.CategoryList
import com.example.foodrecipe.pojo.MealsByCategoryList
import com.example.foodrecipe.pojo.MealsByCategory
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

    private val _popularMeal = MutableStateFlow<List<MealsByCategory>?>(null)
    val popularMeal: StateFlow<List<MealsByCategory>?> = _popularMeal.asStateFlow()

    private val _categoryMeal = MutableStateFlow<List<Category>?>(null)
    val categoryMeal: StateFlow<List<Category>?> = _categoryMeal.asStateFlow()

    fun getRandomMeal() {
        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomMeal = response.body()!!.meals[0]
                    _randomMealData.value = randomMeal
                } else return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })

    }


    fun getPopularItems() {
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object : Callback<MealsByCategoryList> {
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null) {
                    _popularMeal.value = response.body()!!.meals
                } else return
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue( object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    _categoryMeal.value = categoryList.categories
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }
}