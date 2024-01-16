package com.example.universityapp.data.db.userDao

import androidx.lifecycle.LiveData
import com.example.universityapp.data.entity.StudentDB

class UserRepository(private val userDao: UserDao) {

    val userList: LiveData<List<StudentDB>> = userDao.getAllSavedUser()

    suspend fun insertUser(user: StudentDB) {
        userDao.insertUser(user)
    }

    suspend fun getUserById(id: String): StudentDB? {
        return userDao.getUserById(id)
    }

    fun getUserByFIO(fio: String): LiveData<List<StudentDB>> {
        return userDao.getUserByFIO(fio)
    }

    suspend fun deleteUserById(id: String) {
        userDao.deleteUserById(id)
    }

    suspend fun deleteUser(user: StudentDB) = userDao.deleteUser(user)


}