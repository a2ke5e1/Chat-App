package com.a2k.chatapp.repository

import com.a2k.chatapp.models.Message
import com.a2k.chatapp.models.Profile
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileRepo {
    private val _chatRef = Firebase.firestore.collection("profiles")
    fun getProfiles(uid: String): Query {
        return _chatRef.whereNotEqualTo("uid", uid)
    }

    fun getProfile(uid: String): DocumentReference {
        return _chatRef.document(uid)
    }

    fun updateProfile(profile: Profile, uid: String): Task<Void> {
        return _chatRef.document(uid).set(profile)
    }

}