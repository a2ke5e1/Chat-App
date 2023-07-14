package com.a2k.chatapp.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.a2k.chatapp.adapters.MessageAdapter
import com.a2k.chatapp.adapters.ProfileAdapter
import com.a2k.chatapp.databinding.ActivityMainBinding
import com.a2k.chatapp.databinding.ActivityProfileBinding
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.repository.MessageRepo
import com.a2k.chatapp.repository.ProfileRepo
import com.a2k.chatapp.setupUI
import com.a2k.chatapp.viewmodel.MessageViewModel
import com.a2k.chatapp.viewmodel.MessageViewModelFactory
import com.a2k.chatapp.viewmodel.ProfilesViewModel
import com.a2k.chatapp.viewmodel.ProfilesViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    val adapter = ProfileAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(this, window, binding.toolbar)
        setSupportActionBar(binding.toolbar)


        val profileRepo = ProfileRepo()
        val profilesViewModel =
            ViewModelProvider(
                this,
                ProfilesViewModelFactory(
                    profileRepo
                )
            )[ProfilesViewModel::class.java]
        val manager = LinearLayoutManager(this)
        binding.messagesRecyclerView.layoutManager = manager
        binding.messagesRecyclerView.adapter = adapter
        profilesViewModel.profiles.observe(this) {
            adapter.setProfiles(it)
            if (it.isNotEmpty()) {
                binding.noMessageIndicator.visibility = View.GONE
            } else {
                binding.noMessageIndicator.visibility = View.VISIBLE
            }
        }
        profilesViewModel.getProfiles()
        /*binding.logoutButton.setOnClickListener {
            logout()
        }*/

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser == null) {
            val i = Intent(this, SigninScreen::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun logout() {
        // Signs out the user
        auth.signOut()

        // Forces the main activity to recreate
        // to reflect that user has signed out.
        startActivity(Intent(this, SigninScreen::class.java))
        finish();
    }

}