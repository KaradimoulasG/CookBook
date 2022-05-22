package com.example.cookbook.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.databinding.ActivityMealBinding
import com.example.cookbook.db.MealDatabase
import com.example.cookbook.fragments.HomeFragment
import com.example.cookbook.pojo.Meal
import com.example.cookbook.viewModel.MealViewModel
import com.example.cookbook.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMvvm: MealViewModel
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]
//        mealMvvm = ViewModelProvider(this)[MealViewModel::class.java]

        getMealInfoFromIntent()
        setInfoInViews()

        onLoading()
        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutubeClick()
        onFabClick()
    }


    private var savedMeal: Meal ?= null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this, object : Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponse()

                savedMeal = t
                binding.tvCategory.text = "Category : ${t!!.strCategory}"
                binding.tvArea.text = "Area : ${t!!.strArea}"
                binding.tvInstructionsText.text = t!!.strInstructions

                youtubeLink = t.strYoutube.toString()
            }
        })
    }

    private fun getMealInfoFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealView)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
        binding.tvInstructionsText.setTextColor(resources.getColor(R.color.white))
    }

    private fun onYoutubeClick() {
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.fabAdd.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvInstructionsText.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    private fun onResponse(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.fabAdd.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvInstructionsText.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }


    private fun onFabClick() {
        binding.fabAdd.setOnClickListener(){
            savedMeal?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_LONG).show()
            }
        }
    }

}