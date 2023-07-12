package com.a2k.chatapp.repository

import com.a2k.chatapp.models.Message
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessageRepo(private val chatId: String) {
    private val _chatRef = Firebase.firestore.collection("tests")
    fun getMessages(): Query {
        return _chatRef.document(chatId).collection("messages").orderBy("sentDate")
    }

    fun sendMessage(message: Message): Task<DocumentReference> {
        return _chatRef.document(chatId).collection("messages").add(message)
    }

}