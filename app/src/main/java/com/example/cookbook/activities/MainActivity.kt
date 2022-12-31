package com.example.cookbook.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.cookbook.R
import com.example.cookbook.databinding.ActivityMainBinding
import com.example.cookbook.db.MealDatabase
import com.example.cookbook.viewModel.HomeViewModel
import com.example.cookbook.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel: HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelProviderFactory)[HomeViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = Navigation.findNavController(this, R.id.host_fragment)

        NavigationUI.setupWithNavController(binding.btmNav, navController)
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            binding.btmNav.visibility = if (nd.id == R.id.searchFragment) View.GONE else View.VISIBLE
        }

        binding.btmNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    item.title = getString(R.string.btm_menu_home)
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.favouritesFragment -> {
                    item.title = getString(R.string.btm_menu_favorites)
                    navController.navigate(R.id.favouritesFragment)
                    true
                }

                R.id.categoriesFragment -> {
                    item.title = getString(R.string.btm_menu_categories)
                    navController.navigate(R.id.categoriesFragment)
                    true
                }

                else -> false
            }
        }
    }
}


