package com.example.universityapp.mvvm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.data.entity.Message
import com.example.universityapp.data.entity.MessageItem
import com.example.universityapp.data.entity.StudentDB
import com.example.universityapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageMVVM(val token: String, val context: Context?) : ViewModel() {
    private val TAG: String = "MessageMVVM"
    private val mutableMessage: MutableLiveData<Message> = MutableLiveData<Message>()
    private  var userRepository: UserRepository
    private var user: MutableLiveData<StudentDB> = MutableLiveData()

    init {
        val userDao = AppDatabase.getInstance(context!!.applicationContext).userDao()
        userRepository = UserRepository(userDao)
    }

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
    fun setMessage(disciplineId: Int, message: MessageItem) {
        RetrofitInstance.mrsuApi.setForumMessage("Bearer $token", disciplineId, message)
            .enqueue(object : Callback<MessageItem> {
                override fun onResponse(call: Call<MessageItem>, response: Response<MessageItem>) {
                    println("setMessage ${response.body()}")
                    mutableMessage.value!!.add(response.body()!!)
                }

                override fun onFailure(call: Call<MessageItem>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }
            })
    }

    fun observeMessage(): LiveData<Message> {
        return mutableMessage
    }

    fun observeUserById(): MutableLiveData<StudentDB> {
        return user
    }

    fun getUserById(id:String) {
        user.postValue(runBlocking(Dispatchers.IO) {
            userRepository.getUserById(id)
        })
    }
}