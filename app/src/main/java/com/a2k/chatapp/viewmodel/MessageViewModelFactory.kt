package com.a2k.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.repository.MessageRepo

class MessageViewModelFactory(private val repository: MessageRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            MessageViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}