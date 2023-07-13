package com.a2k.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.repository.MessageRepo
import com.a2k.chatapp.repository.ProfileRepo

class ProfilesViewModelFactory(private val repository: ProfileRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfilesViewModel::class.java)) {
            ProfilesViewModel(repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}