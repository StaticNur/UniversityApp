package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.entity.DisciplineX
import com.example.universityapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DisciplineMVVM(val token: String) : ViewModel() {
    private val TAG: String = "DisciplineMVVM"
    private val mutableDiscipline: MutableLiveData<DisciplineX> = MutableLiveData<DisciplineX>()

    fun getDiscipline(year: String, period: String) {
        RetrofitInstance.mrsuApi.getDiscipline("Bearer $token", year, period)
            .enqueue(object : Callback<DisciplineX> {
                override fun onResponse(call: Call<DisciplineX>, response: Response<DisciplineX>) {
                    println(response.body())
                    mutableDiscipline.value = response.body()
                }

                override fun onFailure(call: Call<DisciplineX>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }
            })
    }

    fun observeDiscipline(): LiveData<DisciplineX> {
        return mutableDiscipline
    }
}