package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.entity.Message
import com.example.universityapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageMVVM(val token: String) : ViewModel() {
    private val TAG: String = "MessageMVVM"
    private val mutableMessage: MutableLiveData<Message> = MutableLiveData<Message>()

    fun getMessage(disciplineId: Int) {
        RetrofitInstance.mrsuApi.getForumMessages("Bearer $token", disciplineId)
            .enqueue(object : Callback<Message> {
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    println(response.body())
                    mutableMessage.value = response.body()
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }
            })
    }

    fun observeMessage(): LiveData<Message> {
        return mutableMessage
    }
}