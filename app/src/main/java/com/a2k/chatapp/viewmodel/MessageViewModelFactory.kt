package com.a2k.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a2k.chatapp.models.Message

class MessageViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MessageViewModel::class.java)) {
            MessageViewModel() as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}