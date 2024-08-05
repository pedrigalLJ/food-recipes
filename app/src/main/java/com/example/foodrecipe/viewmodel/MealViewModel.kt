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

class MealViewModel(): ViewModel() {

    private val _mealDetails =  MutableStateFlow<Meal?>(null)
    val mealDetails: StateFlow<Meal?> = _mealDetails.asStateFlow()

    fun getMealDetails(id: String) {
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    _mealDetails.value = response.body()!!.meals[0]
                } else return
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity", t.message.toString())
            }
        })
    }

    fun observeMealDetails(): StateFlow<Meal?> {
        return mealDetails
    }
}