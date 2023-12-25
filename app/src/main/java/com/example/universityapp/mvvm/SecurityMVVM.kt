package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.entity.Security
import com.example.universityapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SecurityMVVM(val token: String) : ViewModel() {
    private val TAG: String = "SecurityMVVM"
    private val mutableSecurity: MutableLiveData<Security> = MutableLiveData<Security>()

    fun getSecurity(date: String) {
        RetrofitInstance.mrsuApi.getSecurity("Bearer $token", date)
            .enqueue(object : Callback<Security> {
                override fun onResponse(call: Call<Security>, response: Response<Security>) {
                    println(response.body())
                    mutableSecurity.value = response.body()
                }

                override fun onFailure(call: Call<Security>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun observeSecurity(): LiveData<Security> {
        return mutableSecurity
    }
}