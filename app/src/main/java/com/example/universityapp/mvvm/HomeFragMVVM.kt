package com.example.universityapp.mvvm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.data.entity.Student
import com.example.universityapp.data.entity.StudentDB
import com.example.universityapp.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragMVVM(val token: String, val context:Context?) : ViewModel() {
    private val TAG = "HomeMVVM"
    private val mutableStudent = MutableLiveData<Student>()
    private var userRepository: UserRepository
    private var allUser: LiveData<List<StudentDB>>

    init {
        getStudent()
        val userDao = AppDatabase.getInstance(context!!.applicationContext).userDao()
        userRepository = UserRepository(userDao)
        allUser = userRepository.userList
    }

    fun getStudent() {
        RetrofitInstance.mrsuApi.getStudent("Bearer $token").enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if(response.body() != null){
                    mutableStudent.value = response.body()
                }else {
                    Toast.makeText(context?.applicationContext,
                        "Ошибка получения данных: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    fun observeHome(): LiveData<Student> {
        return mutableStudent
    }

    fun getAllSavedUser() {
        viewModelScope.launch(Dispatchers.Main) {
        }
    }

    fun insertUser(user: StudentDB) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteUser(user:StudentDB) = viewModelScope.launch(Dispatchers.IO) {
        userRepository.deleteUser(user)
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

    fun deleteUserById(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUserById(id)
        }
    }

    fun deleteAllUsers(){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteAllUsers()
        }
    }
}