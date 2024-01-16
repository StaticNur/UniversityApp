package com.example.universityapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.mvvm.UserPageMVVM

class UserPageCustomFactory(private val token: String, private val context: Context?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserPageMVVM::class.java)) {
            // Ваша логика создания MainFragMVVM, возможно, с использованием someParameter
            return UserPageMVVM(token, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}