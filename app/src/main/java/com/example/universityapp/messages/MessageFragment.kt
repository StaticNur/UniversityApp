package com.example.universityapp.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.universityapp.databinding.FragmentMessagesBinding



class MessageFragment : Fragment() {
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var viewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MessageViewModel::class.java)
        // TODO: Use the ViewModel
        /*// Наблюдаем за изменениями данных  Socet
        viewModel.userData.observe(viewLifecycleOwner, Observer { result ->
            binding.textMessages.text = result
        })
        // Запускаем загрузку данных
        viewModel.fetchUserData()*/

        NetworkTask(binding).execute()
    }
}