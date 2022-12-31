package com.example.cookbook.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.activities.CategoryMealsActivity
import com.example.cookbook.activities.MainActivity
import com.example.cookbook.activities.MealActivity
import com.example.cookbook.adapters.CategoriesAdapter
import com.example.cookbook.adapters.MostPopularAdapter
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.databinding.FragmentHomeBinding
import com.example.cookbook.fragments.bottomView.MealBottomViewFragment
import com.example.cookbook.pojo.MealsByCategory
import com.example.cookbook.pojo.Meal
import com.example.cookbook.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivityViewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.cookbook.fragments.idMeal"
        const val MEAL_NAME = "com.example.cookbook.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.cookbook.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.cookbook.fragments.categoryName"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //prepare RVs
        preparePopularItemsRecyclerView()
        prepareCategoriesRecyclerView()

        //Random Meal
        //mainActivityViewModel.getRandomMeal()
        observeRandomMeal()
        onRandomMealClick()

        //Editor's picks items
        mainActivityViewModel.getPopularItems()
        observePopularItemsLiveData()
        onPopularItemClick()

        //Categories
        mainActivityViewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()

        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }


    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, it.idMeal)
            intent.putExtra(MEAL_NAME, it.strMeal)
            intent.putExtra(MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener(){
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observeRandomMeal() {
        mainActivityViewModel.observeRandomMealLiveData().observe(viewLifecycleOwner)
        {
            Glide.with(this@HomeFragment)
            .load(it!!.strMealThumb)
            .into(binding.imgRandomMeal)

            this.randomMeal = it
        }
    }

    private fun observePopularItemsLiveData() {
        mainActivityViewModel.observePopularItemsLiveData().observe(viewLifecycleOwner
        ) { MealList ->
            popularItemsAdapter.setMeals(mealsByCategoryList = MealList as ArrayList<MealsByCategory>)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.rvPopularMeals.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observeCategoriesLiveData() {
        mainActivityViewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) {
            categoriesAdapter.setCategoryList(it)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }
    }

    private fun onPopularItemLongClick(){
        popularItemsAdapter.onLongItemClick = {
            val mealBottomViewFragment = MealBottomViewFragment.newInstance(it.idMeal)
            mealBottomViewFragment.show(childFragmentManager, "Meal Info")
        }
    }



}