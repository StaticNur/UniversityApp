package com.example.universityapp.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.universityapp.R
import com.example.universityapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/*Из фрагмент в фрагмент
    b_send_second.setOnClickListener{
        val fragment: Fragment = FragmentRight()
        val bundle = Bundle()

        bundle.putSerializable("Message", User("Akbar",19,"+79004332233"))
        fragment.arguments = bundle
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.container_2, fragment)
            .commit()
    }*/
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        setSupportActionBar(binding.toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_schedule,
                R.id.navigation_hub
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}

























