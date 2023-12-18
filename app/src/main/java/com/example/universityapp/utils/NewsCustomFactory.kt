package com.example.universityapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.mvvm.NewsFragMVVM

class NewsCustomFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsFragMVVM::class.java)) {
            // Ваша логика создания MainFragMVVM, возможно, с использованием someParameter
            return NewsFragMVVM(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}