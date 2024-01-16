package com.example.universityapp.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.R
import com.example.universityapp.adapters.SearchAndStudentListAdapter
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.databinding.ActivitySearchAndStudentListBinding
import com.example.universityapp.mvvm.SearchAndStudentListMVVM
import com.example.universityapp.utils.SearchAndStudentListCustomFactory

class SearchAndStudentListActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchAndStudentListBinding
    private lateinit var searchAndStudentListMVVM: SearchAndStudentListMVVM
    private lateinit var searchAndStudentListAdapter: SearchAndStudentListAdapter
    private lateinit var sPref: SharedPreferences
    private lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchAndStudentListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = AppDatabase.getInstance(this).userDao()
        userRepository = UserRepository(userDao)
        searchAndStudentListAdapter = SearchAndStudentListAdapter(binding.recyclerViewUsers, userRepository)

        sPref = this.getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
        searchAndStudentListMVVM = ViewModelProvider(this,
            SearchAndStudentListCustomFactory(sPref.getString("saved_token", "").toString(), this)
        )[SearchAndStudentListMVVM::class.java]

        initView()
    }

    private fun initView() {
        observeUsers()
        prepareRecyclerView()
        onUserClick()
        binding.buttonBack.setOnClickListener {
            finish()
        }

        var inputFio = " "
        var chooseIsStudent: Boolean = true
        searchAndStudentListMVVM.getUsersByFIO(inputFio, chooseIsStudent)

        binding.buttonSearchUser.setOnClickListener {
            inputFio = (binding.etSearchInfo.text ?: " ").toString()
            chooseUserAndSetData(inputFio, chooseIsStudent)
        }
        binding.students.setOnClickListener {
            chooseIsStudent = true
            inputFio = (binding.etSearchInfo.text ?: " ").toString()
            chooseUserAndSetData(inputFio, chooseIsStudent)

        }
        binding.teachers.setOnClickListener {
            chooseIsStudent = false
            inputFio = (binding.etSearchInfo.text ?: " ").toString()
            chooseUserAndSetData(inputFio, chooseIsStudent)
        }
        binding.etSearchInfo.addTextChangedListener{
            inputFio = (binding.etSearchInfo.text ?: " ").toString()
            chooseUserAndSetData(inputFio, chooseIsStudent)
        }
    }

    private fun chooseUserAndSetData(inputFio:String, chooseIsStudent:Boolean){
        if (chooseIsStudent) {
            binding.students.setBackgroundResource(R.drawable.button_choose_background)
            binding.students.setTextColor(ContextCompat.getColor(this, R.color.black))
            binding.teachers.setBackgroundResource(R.drawable.button_background)
            binding.teachers.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            binding.students.setBackgroundResource(R.drawable.button_background)
            binding.students.setTextColor(ContextCompat.getColor(this, R.color.white))
            binding.teachers.setBackgroundResource(R.drawable.button_choose_background)
            binding.teachers.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
        searchAndStudentListMVVM.getUsersByFIO(inputFio, chooseIsStudent)
    }

    private fun observeUsers() {
        searchAndStudentListMVVM.getSearchResult().observe(this) { userInDB ->
            searchAndStudentListAdapter.setStudentList(userInDB)
        }
    }

    private fun onUserClick() {
        searchAndStudentListAdapter.onItemClicked(object : SearchAndStudentListAdapter.OnItemStudentClicked{
            override fun onClickListener(id:String) {
                val intent = Intent(this@SearchAndStudentListActivity, UserPageActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        })
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewUsers.apply {
            adapter = searchAndStudentListAdapter
            layoutManager =
                LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }
}