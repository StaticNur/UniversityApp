package com.example.universityapp.ui.activities

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.universityapp.data.entity.Student
import com.example.universityapp.data.entity.StudentDB
import com.example.universityapp.data.entity.Students
import com.example.universityapp.databinding.ActivityUserPageBinding
import com.example.universityapp.mvvm.UserPageMVVM
import com.example.universityapp.utils.UserPageCustomFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class UserPageActivity: AppCompatActivity() {
    private lateinit var binding: ActivityUserPageBinding
    private lateinit var userPageMVVM: UserPageMVVM
    private lateinit var sPref: SharedPreferences
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        id = intent.getStringExtra("id").toString()
        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        userPageMVVM = ViewModelProvider(this,
            UserPageCustomFactory(sPref.getString("saved_token", "").toString(), this)
        )[UserPageMVVM::class.java]

        initView()
    }

    private fun initView() {
        println("id = $id")
        showLoadingCase()
        observeUser()
        userPageMVVM.getUser(id.toString())
        binding.buttonBack.setOnClickListener {
            finish()
        }
        binding.sendMessage.setOnClickListener {
            Toast.makeText(this, "Не реализована", Toast.LENGTH_SHORT).show()
        }


    }
    private fun observeUser() {
        userPageMVVM.observeUser().observe(this, object : Observer<Students?> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onChanged(s: Students?) {
                if(!s.isNullOrEmpty()){
                    val imageUrl = s[0].Photo?.UrlSource
                    Glide.with(this@UserPageActivity)
                        .load(imageUrl)
                        .into(binding.ivAvatarUser)
                    binding.fioUser.text = s[0].FIO

                    val birthDate = LocalDate.parse(s[0].BirthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    binding.BirthDateUser.text = birthDate.format(formatter)

                    if(s[0].StudentCod.isNullOrEmpty()){
                        binding.studentCodUser.text = s[0].TeacherCod
                        binding.titleCod.text = "TeacherCod:"
                    }else binding.studentCodUser.text = s[0].StudentCod

                    binding.EmailUser.text = s[0].Email

                    if(!userPageMVVM.isUserSavedInDatabase(s[0].Id.toString())){
                        saveUser(s[0])
                    }else println("в бд уже есть этот пользователь: ${s[0]}")
                }else{
                    Toast.makeText(this@UserPageActivity, "Ошибка сервера", Toast.LENGTH_SHORT).show()
                }
                cancelLoadingCase()
            }
        })
    }
    private fun showLoadingCase() {
        //binding.loadingGif.visibility = View.VISIBLE
    }

    fun cancelLoadingCase() {
        //binding.loadingGif.visibility = View.INVISIBLE
    }
    private fun saveUser(student: Student) {
        val user = StudentDB(student.BirthDate.toString(),
            student.Email.toString(),
            student.FIO.toString(),
            student.Id.toString(),
            student.Photo?.UrlSource.toString(),
            student.StudentCod.toString(),
            student.TeacherCod.toString(),)
        userPageMVVM.insertUser(user)
    }
}