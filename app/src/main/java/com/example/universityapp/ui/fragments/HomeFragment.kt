package com.example.universityapp.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.universityapp.R
import com.example.universityapp.data.entity.Student
import com.example.universityapp.data.entity.StudentDB
import com.example.universityapp.databinding.FragmentHomeBinding
import com.example.universityapp.mvvm.HomeFragMVVM
import com.example.universityapp.mvvm.RefreshTokenMVVM
import com.example.universityapp.ui.activities.SignInActivity
import com.example.universityapp.utils.HomeCustomFactory
import okhttp3.ResponseBody
import org.json.JSONObject

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var homeFragMVVM: HomeFragMVVM
    private lateinit var refreshTokenMVVM: RefreshTokenMVVM
    private val SAVED_ACCESS_TOKEN = "saved_token"
    private val SAVED_REFRESH_TOKEN = "saved_refresh_token"
    private val SAVED_DATE_SIGN_IN = "date_sign_in"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingCase()
        refreshTokenMVVM = ViewModelProvider(this)[RefreshTokenMVVM::class.java]

        checkToken()

        homeFragMVVM = ViewModelProvider(this,
            HomeCustomFactory(sPref.getString("saved_token", "").toString(), context))[HomeFragMVVM::class.java]
        observeHome()
        homeFragMVVM.getStudent()
        binding.bLogout.setOnClickListener{
            deleteAllUsers()
            openAuthenticationActivity()
        }
    }

    private fun checkToken() {
        observeNewToken()
        val savedDateString = sPref.getString(SAVED_DATE_SIGN_IN, "")
        if (savedDateString != null && savedDateString.isNotEmpty()) {
            val currentTimeMillis = System.currentTimeMillis()
            val seconds = currentTimeMillis / 1000
            val secondsDifference: Long = seconds - savedDateString.toLong()
            println("Вот столько прожил токен: $secondsDifference")
            if(secondsDifference > 7199){
                refreshTokenMVVM.getRefreshToken(sPref.getString(SAVED_REFRESH_TOKEN,"").toString())
            }
        }else {
            openAuthenticationActivity()  //значит он не зарегистрирован
        }
    }

    private fun observeNewToken() {
        refreshTokenMVVM.observeRefreshToken().observe(viewLifecycleOwner, object : Observer<ResponseBody?> {
            override fun onChanged(t: ResponseBody?) {
                if (t != null) {
                    saveToken(t.string())
                } else {
                    Toast.makeText(context, "Не удалось получить доступ", Toast.LENGTH_SHORT).show()
                    openAuthenticationActivity()
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
        Toast.makeText(context, "New session open", Toast.LENGTH_SHORT).show()
    }


    private fun observeHome() {
        homeFragMVVM.observeHome().observe(viewLifecycleOwner, object : Observer<Student> {
            override fun onChanged(value: Student) {
                if(!homeFragMVVM.isUserSavedInDatabase(value.Id.toString())){
                    sPref.edit().putString("my_id", value.Id.toString()).apply()
                    saveUser(value)
                }
                val avatarImage = binding.ivAvatar
                val imageUrl = value.Photo?.UrlSmall.toString()
                val fIO = value.FIO
                Glide.with(this@HomeFragment)
                    .load(imageUrl)
                    .circleCrop()
                    .into(avatarImage)
                binding.tvNameStudent.text = fIO.toString()
                cancelLoadingCase()
            }
        })
    }

    private fun showLoadingCase() {
        binding.apply {
            ivPhotoMgu.visibility = View.INVISIBLE
            background.visibility = View.INVISIBLE
            items.visibility = View.INVISIBLE
            loadingGif.visibility = View.VISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.g_loading))
        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            ivPhotoMgu.visibility = View.VISIBLE
            background.visibility = View.VISIBLE
            items.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    private fun deleteAllUsers() {
        homeFragMVVM.deleteAllUsers()
    }

    private fun saveUser(student: Student) {
        val user = StudentDB(student.BirthDate.toString(),
            student.Email.toString(),
            student.FIO.toString(),
            student.Id.toString(),
            student.Photo?.UrlSource.toString(),
            student.StudentCod.toString(),
            student.TeacherCod.toString())
        homeFragMVVM.insertUser(user)
    }

    private fun openAuthenticationActivity() {
        val intent = Intent(context, SignInActivity::class.java)
        sPref.edit().putString("saved_token", "").commit()
        sPref.edit().putString("saved_refresh_token", "").commit()
        sPref.edit().putString("date_sign_in", "").commit()
        sPref.edit().putString("my_id", "").commit()
        startActivity(intent)
        activity?.finish()
    }
}