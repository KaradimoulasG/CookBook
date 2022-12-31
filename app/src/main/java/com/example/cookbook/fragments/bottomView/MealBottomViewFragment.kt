package com.example.cookbook.fragments.bottomView

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.cookbook.R
import com.example.cookbook.activities.MainActivity
import com.example.cookbook.activities.MealActivity
import com.example.cookbook.databinding.FragmentCategoriesBinding
import com.example.cookbook.databinding.FragmentMealBottomViewBinding
import com.example.cookbook.fragments.HomeFragment
import com.example.cookbook.viewModel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM1 = "param1"

class MealBottomViewFragment : BottomSheetDialogFragment() {

    private var mealId: String? = null
    private lateinit var binding: FragmentMealBottomViewBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(ARG_PARAM1)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomViewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }
        observeBottomViewMeal()
        onBottomSheetDialogClick()

    }


    private fun onBottomSheetDialogClick() {
        binding.bottmSheet.setOnClickListener{
            if(mealName != null && mealThumb != null){
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply{
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThumb)
                }
                startActivity(intent)
            }
        }
    }

    private var mealName: String? = null
    private var mealThumb: String ?= null
    private fun observeBottomViewMeal() {
        viewModel.observeBottomViewMeal().observe(viewLifecycleOwner, Observer {
            Glide.with(this)
                .load(it.strMealThumb)
                .into(binding.imgBottomView)
            binding.tvBottomViewArea.text = it.strArea
            binding.tvBottomViewCategory.text = it.strCategory
            binding.tvBottomMealName.text = it.strMeal

            mealName = it.strMeal
            mealThumb = it.strMealThumb
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}