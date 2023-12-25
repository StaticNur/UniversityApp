package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.entity.Grade
import com.example.universityapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GradeMVVM(val token: String) : ViewModel() {
    private val TAG: String = "GradeMVVM"
    private val mutableGrade: MutableLiveData<Grade> = MutableLiveData<Grade>()

    fun getGrade(studentId: String) {
        RetrofitInstance.mrsuApi.getGrade("Bearer $token", studentId)
            .enqueue(object : Callback<Grade> {
                override fun onResponse(call: Call<Grade>, response: Response<Grade>) {
                    println(response.body())
                    mutableGrade.value = response.body()
                }

                override fun onFailure(call: Call<Grade>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun observeGrade(): LiveData<Grade> {
        return mutableGrade
    }
}