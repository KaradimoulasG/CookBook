package com.example.cookbook.retrofit

import com.example.cookbook.pojo.CategoryList
import com.example.cookbook.pojo.MealsByCategoryList
import com.example.cookbook.pojo.MealList
import com.example.cookbook.pojo.MealsByCategory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {


    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i")id: String): Call<MealList>

    @GET("filter.php?")
    fun getPopularItems(@Query("c")categoryName: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c")categoryName: String): Call<MealsByCategoryList>

    @GET("filter.php")
    fun getMealsByCountry(@Query("a")countryName: String): Call<MealList>

    @GET("browse.php")
    fun searchForMeals(@Query("s")mealName: String): Call<MealList>
}