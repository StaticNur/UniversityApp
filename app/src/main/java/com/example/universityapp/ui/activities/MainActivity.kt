package com.example.universityapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.universityapp.R
import com.example.universityapp.databinding.ActivityMainBinding
import com.example.universityapp.mvvm.RefreshTokenMVVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.ResponseBody
import org.json.JSONObject

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
    private lateinit var refreshTokenMVVM: RefreshTokenMVVM
    private val SAVED_ACCESS_TOKEN = "saved_token"
    private val SAVED_REFRESH_TOKEN = "saved_refresh_token"
    private val SAVED_DATE_SIGN_IN = "date_sign_in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        refreshTokenMVVM = ViewModelProvider(this)[RefreshTokenMVVM::class.java]

        /*val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(this, R.id.nav_host_fragment_activity_main)
        NavigationUI.setupWithNavController(navView, navController)*/
        // Set the ActionBar
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

        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        initView()
    }

    private fun initView() {
        observeNewToken()

        val savedDateString = sPref.getString(SAVED_DATE_SIGN_IN, "")
        if (savedDateString != null && savedDateString.isNotEmpty()) {
            val currentTimeMillis = System.currentTimeMillis()
            val seconds = currentTimeMillis / 1000
            val secondsDifference: Long = seconds - savedDateString.toLong()
            println("Вот столько прожил токен: $secondsDifference")
            if(secondsDifference > 7199){
                refreshTokenMVVM.getRefreshToken(sPref.getString(SAVED_REFRESH_TOKEN,"").toString())
            }//если добралис до сюдо просто проверка
        }else {
            openAuthenticationActivity()  //значит он не зарегистрирован
        }
    }

    private fun observeNewToken() {
        refreshTokenMVVM.observeRefreshToken().observe(this, object : Observer<ResponseBody?> {
            override fun onChanged(t: ResponseBody?) {
                if (t != null) {
                    saveToken(t.string())
                } else {
                    Toast.makeText(applicationContext, "Не удалось получить доступ", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun saveToken(token: String) {
        val jsonObject = JSONObject(token)
        val accessToken = jsonObject.getString("access_token")
        val refreshToken = jsonObject.getString("refresh_token")
        val ed: SharedPreferences.Editor = sPref.edit()
        ed.putString(SAVED_ACCESS_TOKEN, accessToken)
        ed.putString(SAVED_REFRESH_TOKEN, refreshToken)
        val currentTimeMillis = System.currentTimeMillis()
        val seconds = currentTimeMillis / 1000
        ed.putString(SAVED_DATE_SIGN_IN, seconds.toString()).apply()
        ed.apply()
        Toast.makeText(this, "Session open", Toast.LENGTH_SHORT).show()
    }

    private fun openAuthenticationActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}

























