package com.example.universityapp.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.universityapp.R
import com.example.universityapp.data.entity.Student
import com.example.universityapp.data.entity.UserDB
import com.example.universityapp.databinding.FragmentHomeBinding
import com.example.universityapp.mvvm.HomeFragMVVM
import com.example.universityapp.ui.activities.SignInActivity
import com.example.universityapp.utils.HomeCustomFactory
import pl.droidsonroids.gif.GifImageView

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sPref: SharedPreferences
    private lateinit var homeFragMVVM: HomeFragMVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        sPref = requireContext().getSharedPreferences("MyPref", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoadingCase()
        homeFragMVVM = ViewModelProvider(this,
            HomeCustomFactory(sPref.getString("saved_token", "").toString(), context))[HomeFragMVVM::class.java]

        homeFragMVVM.observeRandomMeal().observe(viewLifecycleOwner, object : Observer<Student> {
            override fun onChanged(value: Student) {
                if(value != null){
                    //saveUser(value)
                    val mealImage = binding.ivAvatar
                    val imageUrl = value.Photo?.UrlSmall.toString()
                    val fIO = value.FIO
                    Glide.with(this@HomeFragment)
                        .load(imageUrl)
                        .circleCrop()
                        .into(mealImage)
                    binding.tvNameStudent.text = fIO.toString()
                    cancelLoadingCase(context)
                }else{
                    cancelLoadingCase(context)
                    Toast.makeText(context, "Не актуальный токен или проблема с сервером. Уже не то)", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.bLogout.setOnClickListener{
            deleteUser()
            openRunTimeSignInActivity()
        }
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

    companion object{
        fun cancelLoadingCase(context:Context?) {
            val activity = context as? Activity
            val ivPhotoMgu = activity?.findViewById(R.id.iv_photo_mgu) as? ImageView
            val background = activity?.findViewById(R.id.background) as? FrameLayout
            val items = activity?.findViewById(R.id.items) as? LinearLayout
            val loadingGif = activity?.findViewById(R.id.loading_gif) as? GifImageView
            val rootHome = activity?.findViewById(R.id.root_home) as? ConstraintLayout
            ivPhotoMgu?.visibility = View.VISIBLE
            background?.visibility = View.VISIBLE
            items?.visibility = View.VISIBLE
            loadingGif?.visibility = View.INVISIBLE
            rootHome?.setBackgroundColor(ContextCompat.getColor(activity, R.color.white))
        }
    }

    private fun openRunTimeSignInActivity(){
        val intent: Intent = Intent(requireActivity(), SignInActivity::class.java)
        sPref.edit().putString("saved_token", "").commit()
        sPref.edit().putString("saved_refresh_token", "").commit()
        sPref.edit().putString("date_sign_in", "").commit()
        startActivity(intent)
    }

    private fun deleteUser() {
        homeFragMVVM.deleteUserById("1")
    }

    private fun saveUser(student: Student) {
        val user = UserDB(student.Id.toString(),
            student.UserName.toString(),
            student.FIO.toString(),
            student.Photo?.UrlSmall.toString())
        homeFragMVVM.insertUser(user)
    }
}