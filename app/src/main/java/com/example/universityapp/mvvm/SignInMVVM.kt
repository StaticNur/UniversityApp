package com.example.universityapp.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.universityapp.data.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInMVVM() : ViewModel() {
    private val TAG: String = "SignInMVVM"
    private val mutableNewToken: MutableLiveData<ResponseBody> = MutableLiveData<ResponseBody>()

    fun getNewToken(email: String, password: String) {
        val clientId = "8"
        val clientSecret = "qweasd"
        val credentials = "$clientId:$clientSecret"
        val base64Credentials = android.util.Base64.encodeToString(
            credentials.toByteArray(),
            android.util.Base64.NO_WRAP
        )

        RetrofitInstance.mrsuAuthApi.getNewToken("Basic $base64Credentials", "password", email, password)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    println(response.body())
                    mutableNewToken.value = response.body()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e(TAG, t.message.toString())
                }

            })
    }

    fun observeNewToken(): LiveData<ResponseBody> {
        return mutableNewToken
    }
}