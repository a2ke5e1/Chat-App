package com.a2k.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.repository.MessageRepo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MessageViewModel(private val repository: MessageRepo): ViewModel() {
    var messages: MutableLiveData<List<Message>> = MutableLiveData()

    fun getMessages(): LiveData<List<Message>> {
        repository.getMessages().addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("MESSAGE_VIEW", "Error loading messages")
            }
            messages.postValue(value!!.toObjects(Message::class.java))
        }
        return messages
    }

    fun sendMessage(message: Message) {
        repository.sendMessage(message)
    }

}