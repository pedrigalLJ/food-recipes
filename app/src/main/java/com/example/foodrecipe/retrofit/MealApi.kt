package com.example.foodrecipe.retrofit

import com.example.foodrecipe.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

}