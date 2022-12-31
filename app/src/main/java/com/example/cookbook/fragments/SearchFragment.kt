package com.example.cookbook.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.activities.CategoryMealsActivity
import com.example.cookbook.activities.MealActivity
import com.example.cookbook.adapters.CategoryMealsAdapter
import com.example.cookbook.databinding.FragmentSearchBinding
import com.example.cookbook.fragments.HomeFragment.Companion.MEAL_ID
import com.example.cookbook.fragments.HomeFragment.Companion.MEAL_NAME
import com.example.cookbook.fragments.HomeFragment.Companion.MEAL_THUMB
import com.example.cookbook.viewModel.CategoryMealsViewModel
import com.example.cookbook.viewModel.MealViewModel
import com.example.cookbook.viewModel.SearchViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var myAdapter: CategoryMealsAdapter
    private lateinit var searchMvvm: SearchViewModel
    private var mealId = ""
    private var mealStr = ""
    private var mealThumb = ""
    private var mealThumb2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = CategoryMealsAdapter()
        searchMvvm = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onSearchClick()
        observeLiveData()
        setOnMealCardClick()
    }

    private fun setOnMealCardClick() {
        binding.searchedMealCard.setOnClickListener {
            val intent = Intent(context, MealActivity:: class.java)
            intent.putExtra(MEAL_ID, mealId)
            intent.putExtra(MEAL_NAME, mealStr)
            intent.putExtra(MEAL_THUMB, mealThumb)
            startActivity(intent)
        }
    }

    private fun observeLiveData() {
        searchMvvm.observSearchLiveData().observe(viewLifecycleOwner) {
            if (it == null) Toast.makeText(
                context,
                "Could find any meals",
                Toast.LENGTH_LONG
            )
                .show()
            else {
                binding.apply {

                    mealId = it.idMeal
                    mealStr = it.strMeal.toString()
                    mealThumb = it.strMealThumb.toString()

                    Glide.with(requireContext().applicationContext)
                        .load(it.strMealThumb)
                        .into(imgSearchedMeal)

                    tvSearchedMeal.text = it.strMeal
                    searchedMealCard.visibility = View.VISIBLE

                }
            }
        }
    }

    private fun onSearchClick() {
        binding.searchBtn.setOnClickListener {
            searchMvvm.searchMeal(binding.etSearch.text.toString(), context)
        }
    }
}