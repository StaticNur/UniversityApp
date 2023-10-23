package com.example.universityapp.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.universityapp.databinding.ActivityMainBinding
import com.example.universityapp.databinding.ActivitySigninBinding

class SignInActivity :AppCompatActivity(){
    private lateinit var binding: ActivitySigninBinding
    private var user: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    fun initView(){
        val bSignin = binding.bSignin
        bSignin.setOnClickListener{
            if(chackInput()){
                openRunTimeActivityFragment()
            }
        }
    }
    fun chackInput():Boolean{
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        var flag = true
        if(etEmail.text.isEmpty()){
            val error = binding.loginError
            error.visibility = View.VISIBLE
            error.text = "Ошибка в логине"
            flag = false
        }else binding.loginError.visibility = View.GONE
        if(etPassword.text.isEmpty()){
            val error = binding.passwordError
            error.visibility = View.VISIBLE
            error.text = "Ошибка в пароли"
            flag = false
        }else binding.passwordError.visibility = View.GONE
        /*if(flag == true){
            println("Do $user")
            user = NetworkTask(binding,
                etEmail.text.toString(),
                etPassword.text.toString()).execute().get()
            println("Posle $user")
            if(user.isNullOrEmpty()){
                flag = false
            }
        }*/
        return flag
    }
    fun openRunTimeActivityFragment(){
        val intent: Intent = Intent(this, ActivityMainBinding::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }

}