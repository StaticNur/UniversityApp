package com.example.universityapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.universityapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_schedule,
                R.id.navigation_hub
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView ,navController)

        /*val bundle = Bundle()
        bundle.putString("user", intent.getStringExtra("user"))

        val homeFragment = HomeFragment()
        homeFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, homeFragment)
            .commit()*/
    }
}