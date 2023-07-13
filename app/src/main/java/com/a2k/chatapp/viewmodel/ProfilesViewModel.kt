package com.a2k.chatapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.models.Profile
import com.a2k.chatapp.repository.MessageRepo
import com.a2k.chatapp.repository.ProfileRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ProfilesViewModel(private val repository: ProfileRepo): ViewModel() {
    var profiles: MutableLiveData<List<Profile>> = MutableLiveData()
    private var _uid = Firebase.auth.currentUser?.uid
    fun getProfiles(): LiveData<List<Profile>> {
        repository.getProfiles(_uid!!).addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("PROFILE_VIEW", "Error loading profiles")
            }
            profiles.postValue(value!!.toObjects(Profile::class.java))
        }
        return profiles
    }

}