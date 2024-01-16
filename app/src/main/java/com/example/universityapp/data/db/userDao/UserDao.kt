package com.example.universityapp.data.db.userDao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.universityapp.data.entity.StudentDB

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: StudentDB)

    @Update
    fun updateUser(user:StudentDB)

    @Query("SELECT * FROM user")
    fun getAllSavedUser():LiveData<List<StudentDB>>

    @Query("SELECT * FROM user WHERE id =:id")
    fun getUserById(id:String):StudentDB

    @Query("SELECT * FROM user WHERE FIO LIKE '%' || :fio || '%'")
    fun getUserByFIO(fio: String): LiveData<List<StudentDB>>

    @Query("DELETE FROM user WHERE id =:id")
    fun deleteUserById(id:String)

    @Delete
    fun deleteUser(user: StudentDB)
}