package com.example.universityapp.auth

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
import com.example.universityapp.MainActivity
import com.example.universityapp.R
import com.example.universityapp.databinding.ActivitySigninBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignInActivity : AppCompatActivity(), TokenCallback {
    private lateinit var binding: ActivitySigninBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var loadingProgressBar: ProgressBar
    private val SAVED_TEXT = "saved_token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        loadingProgressBar = binding.loadingProgressBar

        if (!sPref.getString(SAVED_TEXT, "").isNullOrEmpty()) {
            openRunTimeActivityFragment()
        } else {
            // TODO: проверка срока сессии
        }
        initView()
    }

    fun initView() {
        val bSignin = binding.bSignin
        bSignin.setOnClickListener {
            showLoader()
            chackInput()
        }
        val etPassword: EditText = binding.etPassword
        etPassword.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (etPassword.right - etPassword.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // Касание произошло на элементе "глазика"
                    if (etPassword.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        // Показываем пароль
                        etPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_eye_invisible, 0)
                    } else {
                        // Скрываем пароль
                        etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_eye_visible, 0)
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun chackInput() {
        val flag = checkNotEmpty()
        if (flag) {
            setToken()
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

    private fun setToken() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    TokenRepository().getAccessToken(
                        binding.etEmail.text.toString(),
                        binding.etPassword.text.toString()
                    )
                }

                if (result == "500") {
                    val error = binding.passwordError
                    error.visibility = View.VISIBLE
                    error.text = "Пользователя с таким email/password НЕТ!"
                } else {
                    saveToken(result)
                }
                onTokenSet(result != null && result != "500")

            } catch (e: Exception) {
                // Handle exceptions here
            } finally {
                hideLoader()
            }
        }
    }

    private fun saveToken(token: String?) {
        val ed: SharedPreferences.Editor = sPref.edit()
        ed.putString(SAVED_TEXT, token)
        ed.apply()
        Toast.makeText(this, "Session open", Toast.LENGTH_SHORT).show()
    }

    override fun onTokenSet(flag: Boolean) {
        if (flag) {
            openRunTimeActivityFragment()
        }
    }

    private fun showLoader() {
        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        loadingProgressBar.visibility = View.GONE
    }
    private fun openRunTimeActivityFragment(){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}