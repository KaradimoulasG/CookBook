package com.example.cookbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.databinding.MealItemBinding
import com.example.cookbook.pojo.Category
import com.example.cookbook.pojo.Meal
import com.example.cookbook.pojo.MealList
import com.example.cookbook.pojo.MealsByCategory

class FavouritesMealsAdapter: RecyclerView.Adapter<FavouritesMealsAdapter.FavouritesMealsAdapterViewHolder>() {

//    private var favouriteMeals: List<Meal> = ArrayList()
//
//    fun setFavouritesMealsList(favouriteMeals: List<Meal>){
//        this.favouriteMeals = favouriteMeals
//        notifyDataSetChanged()
//    }

    lateinit var onItemClick: ((Meal) -> Unit)

    inner class FavouritesMealsAdapterViewHolder(val binding: MealItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesMealsAdapterViewHolder {
        return FavouritesMealsAdapterViewHolder(
            MealItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavouritesMealsAdapterViewHolder, position: Int) {
        val meal = differ.currentList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = meal.strMeal

        holder.itemView.setOnClickListener(){
            onItemClick.invoke(meal)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}