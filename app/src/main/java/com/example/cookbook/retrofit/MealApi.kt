package com.example.cookbook.retrofit

import com.example.cookbook.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET

interface MealApi {


    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

}