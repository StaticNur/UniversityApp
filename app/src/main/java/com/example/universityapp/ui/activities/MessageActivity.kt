package com.example.universityapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.universityapp.adapters.MessageAdapter
import com.example.universityapp.data.db.AppDatabase
import com.example.universityapp.data.db.userDao.UserRepository
import com.example.universityapp.data.entity.Message
import com.example.universityapp.data.entity.MessageItem
import com.example.universityapp.data.entity.Photo
import com.example.universityapp.data.entity.StudentDB
import com.example.universityapp.data.entity.User
import com.example.universityapp.databinding.ActivityMessageBinding
import com.example.universityapp.mvvm.MessageMVVM
import com.example.universityapp.utils.MessageCustomFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File
import java.time.LocalDateTime

class MessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMessageBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var messageMVVM: MessageMVVM
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var userRepository: UserRepository
    private var disciplineId: Int = 0
    private val PICK_IMAGE_REQUEST = 1
    private val PICK_FILE_REQUEST = 2
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabScrollToBottom: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMessageBinding.inflate(layoutInflater)
        sPref = this.getSharedPreferences("MyPref", MODE_PRIVATE)
        setContentView(binding.root)

        val userDao = AppDatabase.getInstance(this).userDao()
        userRepository = UserRepository(userDao)

        recyclerView = binding.recyclerViewChat
        fabScrollToBottom = binding.fabScrollToBottom

        messageAdapter = MessageAdapter(binding.recyclerViewChat, userRepository)
        messageMVVM = ViewModelProvider(
            this,
            MessageCustomFactory(sPref.getString("saved_token", "").toString(), this)
        )[MessageMVVM::class.java]
        disciplineId = intent.getIntExtra("id", 1400122)

        initView()
    }

    private fun initView() {
        showLoadingCase()
        observeMessage()
        observeUserById()
        prepareRecyclerView()
        onUserClick()
        messageMVVM.getMessage(disciplineId)

        binding.buttonBack.setOnClickListener {
            finish()
        }
        binding.buttonRefresh.setOnClickListener {
            showLoadingCase()
            messageMVVM.getMessage(disciplineId)
        }
        binding.buttonSendMessage.setOnClickListener {
            val messageBody = binding.editTextMessage.text
            if (!messageBody.isNullOrEmpty()) {
                val myId = sPref.getString("my_id", "").toString()
                messageMVVM.getUserById(myId)
                Toast.makeText(
                    this,
                    "сообщение отправлено",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Нельзя отправлять пустое сообщение",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.buttonAttachFile.setOnClickListener {
            // Создаем Intent для выбора изображения
            val imageIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            // Создаем Intent для выбора файла
            val fileIntent = Intent(Intent.ACTION_GET_CONTENT)
            fileIntent.type = "*/*"

            // Создаем Intent, объединяя оба предыдущих
            val chooserIntent = Intent.createChooser(imageIntent, "Choose File")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(fileIntent))

            startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST)
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    // Прокручивание вверх
                    fabScrollToBottom.show()
                } else {
                    // Прокручивание вниз
                    fabScrollToBottom.hide()
                }
                if (isLastItemVisible()) {
                    //мы в самом низу
                    fabScrollToBottom.hide()
                }
            }
        })

        fabScrollToBottom.setOnClickListener {
            recyclerView.smoothScrollToPosition(messageAdapter.itemCount - 1)
        }
    }

    private fun isLastItemVisible(): Boolean {
        val layoutManager = recyclerView.layoutManager
        val lastVisibleItemPosition =
            (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        val totalItemCount = layoutManager.itemCount

        return lastVisibleItemPosition == totalItemCount - 1 // Проверка на "достаточное" приближение к последнему элементу
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    val selectedImageUri: Uri? = data?.data
                    binding.imageViewPreview.setImageURI(selectedImageUri)
                }

                PICK_FILE_REQUEST -> {
                    data?.data?.let { fileUri ->
                        val file = File(fileUri.path)
                        val fileName = file.name
                        val fileFormat = fileName.substringAfterLast('.', "")
                        val fileSize = file.length()

                        binding.editTextMessage
                            .setText("File Info:\nName: $fileName\nFormat: $fileFormat\nSize: $fileSize bytes")
                    }
                }
            }
        }
    }

    private fun observeMessage() {
        messageMVVM.observeMessage().observe(this, object : Observer<Message?> {
            override fun onChanged(m: Message?) {
                if (!m.isNullOrEmpty()) {
                    messageAdapter.setMessageList(m, sPref)
                } else {
                    fabScrollToBottom.hide()
                    binding.emptyMessage.visibility = View.VISIBLE
                }
                hideLoadingCase()
            }
        })
    }

    private fun observeUserById() {
        messageMVVM.observeUserById().observe(this, object : Observer<StudentDB> {
            override fun onChanged(m: StudentDB) {
                val currentDateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    LocalDateTime.now()
                } else {
                    TODO("VERSION.SDK_INT < O")
                }
                val messageBody = binding.editTextMessage.text
                val user = convertStudentDBToUser(m)
                val newMessage = MessageItem(
                    currentDateTime.toString(),
                    null,
                    null,
                    messageBody.toString(),
                    user
                )
                messageMVVM.setMessage(disciplineId, newMessage)
                binding.editTextMessage.text.clear()
                messageAdapter.addMessage(newMessage)
            }
        })
    }

    private fun onUserClick() {
        messageAdapter.onItemClicked(object : MessageAdapter.OnItemUserClicked {
            override fun onClickListener(id: String) {
                val intent = Intent(this@MessageActivity, UserPageActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        })
    }

    private fun showLoadingCase() {
        binding.apply {
            loadingGif.visibility = View.VISIBLE
            emptyMessage.visibility = View.INVISIBLE
            recyclerViewChat.visibility = View.INVISIBLE
        }
    }

    private fun hideLoadingCase() {
        binding.apply {
            loadingGif.visibility = View.INVISIBLE
            recyclerViewChat.visibility = View.VISIBLE
        }
    }

    private fun prepareRecyclerView() {
        binding.recyclerViewChat.apply {
            adapter = messageAdapter
            layoutManager =
                LinearLayoutManager(context) //GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

    private fun convertStudentDBToUser(student: StudentDB): User {
        return User(
            student.Id,
            student.FIO.toString(),
            Photo("", student.Photo, ""),
            ""
        )
    }
}