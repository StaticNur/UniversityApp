package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.entity.Student
import com.example.universityapp.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class HomeFragMVVM(val token: String) : ViewModel() {
    private val TAG = "HomeMVVM"
    private val mutableStudent = MutableLiveData<Student>()
    init {
        getStudent()
    }

    private fun getStudent() {
        RetrofitInstance.mrsuApi.getStudent("Bearer $token").enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                mutableStudent.value = response.body()
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    fun observeRandomMeal(): LiveData<Student> {
        return mutableStudent
    }
}