package com.example.universityapp.hub

import androidx.lifecycle.ViewModel

class HubViewModel : ViewModel() {
    /*private val repository = UserRepository()  // Вам нужно создать репозиторий для выполнения запросов

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
    }*/
}