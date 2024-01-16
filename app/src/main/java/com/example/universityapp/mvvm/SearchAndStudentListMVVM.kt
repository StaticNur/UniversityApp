package com.example.universityapp.mvvm

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.data.entity.StudentDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SearchAndStudentListMVVM(val token: String, val context: Context?) : ViewModel() {
    private val TAG: String = "SearchAndStudentListMVVM"
    private  var userRepository: UserRepository
    private var searchResult: MutableLiveData<List<StudentDB>> = MutableLiveData()

    init {
        val userDao = AppDatabase.getInstance(context!!.applicationContext).userDao()
        userRepository = UserRepository(userDao)
    }

    fun getUsersByFIO(fio: String, isStudent:Boolean) {
        userRepository.getUserByFIO(fio).observe(context as LifecycleOwner) { result ->
            if (isStudent) {
                val studentList = result.filter { it.TeacherCod == "null" }
                searchResult.value = studentList
                println("student: $studentList")
            } else {
                val teacherList = result.filter { it.StudentCod == "null" }
                searchResult.value = teacherList
                println("teacherList : $teacherList")
            }
        }
    }

    fun getSearchResult(): MutableLiveData<List<StudentDB>> {
        return searchResult
    }

    fun insertUser(user: StudentDB) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insertUser(user)
            withContext(Dispatchers.Main) {
            }
        }
    }

    fun deleteUser(user: StudentDB) = viewModelScope.launch(Dispatchers.IO) {
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
}