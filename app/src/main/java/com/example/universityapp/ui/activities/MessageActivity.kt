package com.example.universityapp.ui.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.universityapp.adapters.MessageAdapter
import com.example.universityapp.data.entity.Message
import com.example.universityapp.databinding.ActivityMessageBinding
import com.example.universityapp.mvvm.MessageMVVM
import com.example.universityapp.utils.MessageCustomFactory

class MessageActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMessageBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var messageMVVM: MessageMVVM
    private lateinit var messageAdapter: MessageAdapter
    private var disciplineId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        sPref = this.getSharedPreferences("MyPref", MODE_PRIVATE)
        setContentView(binding.root)
        messageAdapter = MessageAdapter()
        messageMVVM = ViewModelProvider(this,
            MessageCustomFactory(sPref.getString("saved_token", "").toString()))[MessageMVVM::class.java]
        disciplineId = intent.getIntExtra("id",1400122)

        initView()
    }

    private fun initView() {
        observeMessage()
        prepareRecyclerView()
        messageMVVM.getMessage(disciplineId)

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun observeMessage() {
        messageMVVM.observeMessage().observe(this, object : Observer<Message?> {
            override fun onChanged(t: Message?) {
                if (true) {
                    messageAdapter.setMessageList(t!!)
                } else {
                    Toast.makeText(applicationContext, "Не удалось получить сообщения", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun prepareRecyclerView() {
        binding.recyclerViewChat.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

}