package com.example.universityapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.mvvm.HomeFragMVVM

class HomeCustomFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragMVVM::class.java)) {
            // Ваша логика создания MainFragMVVM, возможно, с использованием someParameter
            return HomeFragMVVM(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}