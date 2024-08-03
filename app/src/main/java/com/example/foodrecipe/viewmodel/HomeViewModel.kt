package com.example.foodrecipe.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
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

    fun observeRandomMealData(): StateFlow<Meal?> {
        return randomMealData
    }
}