package com.example.universityapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.mvvm.GradeMVVM

class GradeCustomFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GradeMVVM::class.java)) {
            // Ваша логика создания MainFragMVVM, возможно, с использованием someParameter
            return GradeMVVM(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}