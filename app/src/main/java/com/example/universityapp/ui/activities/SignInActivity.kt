package com.example.universityapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.R
import com.example.universityapp.databinding.ActivitySigninBinding
import com.example.universityapp.mvvm.SignInMVVM
import okhttp3.ResponseBody
import org.json.JSONObject


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var signeInMVVM:SignInMVVM
    private val SAVED_ACCESS_TOKEN = "saved_token"
    private val SAVED_REFRESH_TOKEN = "saved_refresh_token"
    private val SAVED_DATE_SIGN_IN = "date_sign_in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signeInMVVM = ViewModelProvider(this)[SignInMVVM::class.java]
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        loadingProgressBar = binding.loadingProgressBar

        // TODO: проверка срока сессии                  mvp         mvc         mvi        bloc
        //  adi             dagger Icontrol
        //  reflactia ne nado               (C) KDN                DAL             SQLite
        //  сценари транзакции     актив рекертс         DDl         ROOM(RUM) orm       Green Down
        // Single R           Dot net                 Single R  aspanet
        // hub   kakoy to klyuch
        //
        //               пишем свой DI f на java
        //
        //
        //
        //


        initView()
    }

    fun initView() {
        observeNewToken()
        handlerVisiblePassword()
        val bSignin = binding.bSignin
        bSignin.setOnClickListener {
            showLoader()
            if(checkNotEmpty()){
                signeInMVVM.getNewToken(binding.etEmail.text.toString(),binding.etPassword.text.toString())
            }
        }
    }

    private fun handlerVisiblePassword(){
        val etPassword: EditText = binding.etPassword
        etPassword.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (etPassword.right - etPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Касание произошло на элементе "глазика"
                    if (etPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        // Показываем пароль
                        etPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.mipmap.ic_eye_invisible,
                            0
                        )
                    } else {
                        // Скрываем пароль
                        etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.mipmap.ic_eye_visible,
                            0
                        )
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun checkNotEmpty(): Boolean {
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        var flag = true

        if (etEmail.text.isEmpty()) {
            val error = binding.loginError
            error.visibility = View.VISIBLE
            error.text = "Ошибка в логине"
            flag = false
            hideLoader()
        } else binding.loginError.visibility = View.GONE

        if (etPassword.text.isEmpty()) {
            val error = binding.passwordError
            error.visibility = View.VISIBLE
            error.text = "Ошибка в пароли"
            flag = false
            hideLoader()
        } else binding.passwordError.visibility = View.GONE

        return flag
    }

    private fun observeNewToken() {
        signeInMVVM.observeNewToken().observe(this, object : Observer<ResponseBody?> {
            override fun onChanged(t: ResponseBody?) {
                if (t != null) {
                    saveToken(t.string())
                    hideLoader()
                    openRunTimeActivityFragment()
                } else {
                    hideLoader()
                    val error = binding.passwordError
                    error.visibility = View.VISIBLE
                    error.text = "Пользователя с таким email/password НЕТ!"
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

    private fun showLoader() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        loadingProgressBar.visibility = View.GONE
    }

    private fun openRunTimeActivityFragment() {
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}