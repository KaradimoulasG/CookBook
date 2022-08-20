package com.example.cookbook.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cookbook.databinding.MealItemBinding
import com.example.cookbook.pojo.Category
import com.example.cookbook.pojo.MealsByCategory

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewHolder>(){

    private var mealsList = ArrayList<MealsByCategory>()
    private var categoryList:List<Category> = ArrayList()
    lateinit var onItemClick: ((MealsByCategory) -> Unit)



    fun setMealsList(mealsList: List<MealsByCategory>){
        this.mealsList = mealsList as ArrayList<MealsByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewHolder(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewHolder {
        return CategoryMealsViewHolder(
                MealItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(mealsList[position].strMealThumb)
            .into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal

        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    fun onItemClicked(onItemClick: OnItemCategoryClicked){
        this.onItemClick = onItemClick
    }

    interface OnItemCategoryClicked : (MealsByCategory) -> Unit {
        fun onClickListener(category: MealsByCategory)
    }


}