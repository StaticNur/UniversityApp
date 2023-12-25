package com.example.easyfood.data.db

import androidx.lifecycle.LiveData
import com.example.universityapp.data.entity.UserDB

class Repository(private val userDao: Dao) {

    val userList: LiveData<List<UserDB>> = userDao.getAllSavedUser()

    suspend fun insertUser(user: UserDB) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(id: String): UserDB {
        return userDao.getUserById(id)
    }

    suspend fun deleteUserById(id: String) {
        userDao.deleteUserById(id)
    }

    suspend fun deleteUser(user: UserDB) = userDao.deleteUser(user)


}