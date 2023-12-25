package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.entity.Schedule
import com.example.universityapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ScheduleFragMVVM(val token: String) : ViewModel() {
    private val TAG: String = "ScheduleMVVM"
    private val mutableStudent:MutableLiveData<Schedule> = MutableLiveData<Schedule>()

    fun getSchedule(date: String?) {
        RetrofitInstance.mrsuApi.getSchedule("Bearer $token", date)
            .enqueue(object : Callback<Schedule> {
                override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                    println(response.body())
                    mutableStudent.value = response.body()
                }

                override fun onFailure(call: Call<Schedule>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun observeSchedule(): LiveData<Schedule> {
        return mutableStudent
    }
}