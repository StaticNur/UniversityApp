package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.entity.News
import com.example.universityapp.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsFragMVVM(val token: String) : ViewModel() {
    private val TAG: String = "NewsMVVM"
    private val mutableStudent:MutableLiveData<News> = MutableLiveData<News>()

    init {
        getNews()
    }

    fun getNews() {
        RetrofitInstance.mrsuApi.getNews("Bearer $token")
            .enqueue(object : Callback<News> {
                override fun onResponse(call: Call<News>, response: Response<News>) {
                    println(response.body())
                    mutableStudent.value = response.body()
                }

                override fun onFailure(call: Call<News>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun observeNews(): LiveData<News> {
        return mutableStudent
    }
}