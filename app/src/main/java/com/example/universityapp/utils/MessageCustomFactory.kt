package com.example.universityapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.mvvm.MessageMVVM

class MessageCustomFactory(private val token: String, private val context: Context?) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageMVVM::class.java)) {
            // Ваша логика создания MainFragMVVM, возможно, с использованием someParameter
            return MessageMVVM(token, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}