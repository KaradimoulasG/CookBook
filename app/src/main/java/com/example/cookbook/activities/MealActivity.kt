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
import timber.log.Timber

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMvvm: MealViewModel
    private var youtubeLink: String? = null
    private var youtubeVideoDeleted: Boolean = false
    private var savedMeal: Meal ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        getMealInfoFromIntent()
        setInfoInViews()
        onLoading()
    }

    override fun onResume() {
        super.onResume()

        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutubeClick()
        onFabClick()
    }

    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this) { t ->
            onResponse()

            savedMeal = t
            binding.tvCategory.text = getString(R.string.meal_act_category, t.strCategory)
            binding.tvArea.text = getString(R.string.meal_act_area, t.strArea)
            binding.tvInstructionsText.text = t.strInstructions

            if (t.strYoutube.isNullOrEmpty()) youtubeVideoDeleted = true
            else youtubeLink = t.strYoutube.toString()
        }
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
        binding.collapsingToolbar.setCollapsedTitleTextColor(getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(getColor(R.color.white))
        binding.tvInstructionsText.setTextColor(getColor(R.color.white))
    }

    private fun onYoutubeClick() {

        binding.imgYoutube.setOnClickListener{
            when (youtubeVideoDeleted) {
                false -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
                    startActivity(intent)
                }
                true -> Toast.makeText(this, getString(R.string.deleted_youtube_video), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onLoading(){
        binding.apply {
            progressBar.visibility = View.VISIBLE
            fabAdd.visibility = View.INVISIBLE
            tvInstructions.visibility = View.INVISIBLE
            tvCategory.visibility = View.INVISIBLE
            tvArea.visibility = View.INVISIBLE
            tvInstructionsText.visibility = View.INVISIBLE
            imgYoutube.visibility = View.INVISIBLE
        }
    }

    private fun onResponse(){
        binding.apply {
            progressBar.visibility = View.INVISIBLE
            fabAdd.visibility = View.VISIBLE
            tvInstructions.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            tvArea.visibility = View.VISIBLE
            tvInstructionsText.visibility = View.VISIBLE
            imgYoutube.visibility = View.VISIBLE
        }
    }


    private fun onFabClick() {
        binding.fabAdd.setOnClickListener {
            savedMeal?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, getString(R.string.meal_saved), Toast.LENGTH_LONG).show()
            }
        }
    }

}