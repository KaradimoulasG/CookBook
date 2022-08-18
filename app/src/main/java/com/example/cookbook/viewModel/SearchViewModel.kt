package com.example.cookbook.viewModel

import android.content.Context
import android.nfc.Tag
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cookbook.pojo.Meal
import com.example.cookbook.pojo.RandomMealResponse
import com.example.cookbook.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SearchViewModel : ViewModel() {

    private var searchedMealLiveData = MutableLiveData<Meal>()

    fun searchMeal(name: String, context: Context?) {
        RetrofitInstance.api.searchForMeals(name)
            .enqueue(object : retrofit2.Callback<RandomMealResponse> {
                override fun onResponse(
                    call: Call<RandomMealResponse>,
                    response: Response<RandomMealResponse>
                ) {
                    if (response.body()?.meals == null) {
                        Toast.makeText(
                            context?.applicationContext,
                            "Could not find any meals",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else searchedMealLiveData.value = response.body()!!.meals[0]

                }

                override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                    Log.e("Search MVVM", t.message.toString())
                }
            })
    }

    fun observSearchLiveData() : LiveData<Meal> {
        return searchedMealLiveData
    }

}