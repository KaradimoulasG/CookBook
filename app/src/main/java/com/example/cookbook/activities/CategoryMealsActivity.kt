package com.example.cookbook.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cookbook.R
import com.example.cookbook.adapters.CategoryMealsAdapter
import com.example.cookbook.databinding.ActivityCategoryMealsBinding
import com.example.cookbook.databinding.ActivityMealBinding
import com.example.cookbook.fragments.HomeFragment
import com.example.cookbook.pojo.Category
import com.example.cookbook.pojo.Meal
import com.example.cookbook.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel: CategoryMealsViewModel
    lateinit var categoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]
        categoryMealsViewModel.getMealsByCategoryName(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer {
            binding.tvCategoryCount.text = it.size.toString()
            categoryMealsAdapter.setMealsList(it)
        })
    }

    override fun onResume() {
        super.onResume()

        onItemClick()
    }

    private fun prepareRecyclerView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealsAdapter
        }
    }


    private fun onItemClick() {
        categoryMealsAdapter.onItemClick = {
            val intent = Intent(applicationContext, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }


    companion object {
        const val CATEGORY_NAME = "com.example.cookbook.fragments.nameCategory"

    }
}