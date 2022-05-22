package com.example.cookbook.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cookbook.pojo.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealInfo ORDER BY dateModified")
    fun getAllMeals(): LiveData<List<Meal>>
}