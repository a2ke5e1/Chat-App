package com.a2k.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.a2k.chatapp.models.Message
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MessageViewModel: ViewModel() {
    var messages: MutableLiveData<List<Message>> = MutableLiveData()
    private val _chatRef = Firebase.firestore.collection("chats")
    fun getMessages(): LiveData<List<Message>> {
        _chatRef.orderBy("sentDate").addSnapshotListener { value, error ->
            if (error != null) {

            }
            value
            messages.postValue(value!!.toObjects(Message::class.java))
        }
        return messages
    }

    fun sendMessage(message: Message) {
        Firebase.firestore.collection("chats").add(message)
    }

}