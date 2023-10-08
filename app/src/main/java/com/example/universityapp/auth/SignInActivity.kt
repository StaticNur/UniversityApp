package com.example.universityapp.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.universityapp.MainActivity
import com.example.universityapp.R

class SignInActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        initView()
    }
    fun initView(){
        val b_signin = findViewById<Button>(R.id.b_signin)
        b_signin.setOnClickListener{
            openRunTimeActivityFragment()
        }
    }
    fun openRunTimeActivityFragment(){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}