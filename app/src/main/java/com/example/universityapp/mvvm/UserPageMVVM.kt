package com.example.universityapp.mvvm

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.data.entity.StudentDB
import com.example.universityapp.data.entity.Students
import com.example.universityapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPageMVVM(val token: String, val context: Context?) : ViewModel() {
    private val TAG: String = "UserPageMVVM"
    private val mutableUser: MutableLiveData<Students> = MutableLiveData<Students>()
    private var userRepository: UserRepository
    private var allUser: LiveData<List<StudentDB>>

    init {
        val mealDao = AppDatabase.getInstance(context!!.applicationContext).userDao()
        userRepository = UserRepository(mealDao)
        allUser = userRepository.userList
    }

    fun getUser(uids: String?) {
        RetrofitInstance.mrsuApi.getUserById("Bearer $token", uids)
            .enqueue(object : Callback<Students> {
                override fun onResponse(call: Call<Students>, response: Response<Students>) {
                    println(response.body())
                    mutableUser.value = response.body()
                }

                override fun onFailure(call: Call<Students>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun observeUser(): LiveData<Students> {
        return mutableUser
    }

    fun insertUser(user: StudentDB) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun isUserSavedInDatabase(id: String): Boolean {
        var user: StudentDB? = null
        runBlocking(Dispatchers.IO) {
            user = userRepository.getUserById(id)
        }
        if (user == null)
            return false
        return true

    }
}