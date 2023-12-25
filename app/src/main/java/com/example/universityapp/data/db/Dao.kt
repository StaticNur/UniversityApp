package com.example.easyfood.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.universityapp.data.entity.UserDB

@Dao
interface Dao {
    @Insert
    fun insertUser(user: UserDB)

    @Update
    fun updateUser(user:UserDB)

    @Query("SELECT * FROM user order by id asc")
    fun getAllSavedUser():LiveData<List<UserDB>>

    @Query("SELECT * FROM user WHERE id =:id")
    fun getUserById(id:String):UserDB

    @Query("DELETE FROM user WHERE id =:id")
    fun deleteUserById(id:String)

    @Delete
    fun deleteUser(user:UserDB)
}