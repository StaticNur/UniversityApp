package com.example.universityapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }
    fun initView(){
        val b_signin = findViewById<Button>(R.id.b_signin)
        b_signin.setOnClickListener{
            openRunTimeActivityFragment()
        }
    }
    fun openRunTimeActivityFragment(){
        val intent: Intent = Intent(this, PersonalAreaActivity::class.java)
        startActivity(intent)
    }
}