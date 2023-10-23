package com.example.universityapp.messages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.universityapp.messages.socket.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val repository = UserRepository()  // Вам нужно создать репозиторий для выполнения запросов

    private val _userData = MutableLiveData<String>()
    val userData: LiveData<String> get() = _userData

    fun fetchUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.getUserData { result ->
                    _userData.postValue(result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _userData.postValue("Error: ${e.message}")
            }
        }
    }
}