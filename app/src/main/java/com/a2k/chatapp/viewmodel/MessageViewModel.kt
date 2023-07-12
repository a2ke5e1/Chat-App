package com.a2k.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.repository.MessageRepo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MessageViewModel(private val repository: MessageRepo, private val chatId: String): ViewModel() {
    var messages: MutableLiveData<List<Message>> = MutableLiveData()

    fun getMessages(): LiveData<List<Message>> {
        repository.getMessages(chatId).addSnapshotListener { value, error ->
            if (error != null) {

            }
            messages.postValue(value!!.toObjects(Message::class.java))
        }
        return messages
    }

    fun sendMessage(message: Message) {
        repository.sendMessage(message, chatId)
    }

}