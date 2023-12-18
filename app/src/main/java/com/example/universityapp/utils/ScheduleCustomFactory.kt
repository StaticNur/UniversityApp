package com.example.universityapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.mvvm.ScheduleFragMVVM

class ScheduleCustomFactory(private val token: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScheduleFragMVVM::class.java)) {
            // Ваша логика создания MainFragMVVM, возможно, с использованием someParameter
            return ScheduleFragMVVM(token) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}