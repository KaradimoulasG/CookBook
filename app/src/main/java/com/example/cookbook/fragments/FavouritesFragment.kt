package com.example.cookbook.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cookbook.activities.MainActivity
import com.example.cookbook.activities.MealActivity
import com.example.cookbook.adapters.FavouritesMealsAdapter
import com.example.cookbook.databinding.FragmentFavouritesBinding
import com.example.cookbook.pojo.Meal
import com.example.cookbook.viewModel.HomeViewModel


class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favouritesAdapter: FavouritesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()

        observeFavourites()
        onFavouriteMealClick()
    }

    private fun onFavouriteMealClick() {
        favouritesAdapter.onItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, it.strMealThumb)
            startActivity(intent)
        }
    }


    private fun observeFavourites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer {
            favouritesAdapter.differ.submitList(it)
        })
    }

    private fun prepareRecyclerView() {
        favouritesAdapter = FavouritesMealsAdapter()
        binding.rvFavourites.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = favouritesAdapter
        }
    }
}
