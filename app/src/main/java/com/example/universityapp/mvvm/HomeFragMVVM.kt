package com.example.universityapp.mvvm

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.data.db.Repository
import com.example.easyfood.data.db.UserDatabase
import com.example.universityapp.data.entity.Student
import com.example.universityapp.data.entity.UserDB
import com.example.universityapp.retrofit.RetrofitInstance
import com.example.universityapp.ui.fragments.HomeFragment
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
    private  var repository: Repository
    private  var allUser: LiveData<List<UserDB>>

    init {
        getStudent()
        val mealDao = UserDatabase.getInstance(context!!.applicationContext).dao()
        repository = Repository(mealDao)
        allUser = repository.userList
    }

    private fun getStudent() {
        RetrofitInstance.mrsuApi.getStudent("Bearer $token").enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if(response.body() != null){
                    mutableStudent.value = response.body()
                }else {
                    HomeFragment.cancelLoadingCase(context)
                    Toast.makeText(context?.applicationContext, "Истек срок действия токена", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    fun observeRandomMeal(): LiveData<Student> {
        return mutableStudent
    }
    fun getAllSavedUser() {
        viewModelScope.launch(Dispatchers.Main) {
        }
    }

    fun insertUser(user: UserDB) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteUser(user:UserDB) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteUser(user)
    }
    fun isUserSavedInDatabase(id: String): Boolean {
        var user: UserDB? = null
        runBlocking(Dispatchers.IO) {
            user = repository.getUserById(id)
        }
        if (user == null)
            return false
        return true

    }

    fun deleteUserById(id:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteUserById(id)
        }
    }
}