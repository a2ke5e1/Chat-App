package com.a2k.chatapp.screens

import android.content.Intent
import android.os.Bundle
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
    // val adapter = MessageAdapter()
    val adapter = ProfileAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
        }
        profilesViewModel.getProfiles()

        /*val messageRepo = MessageRepo("3d0452179cd0c3cdb4814b9c96b6b9a459c655df557a38d34bf98988688c050a")
        val messageViewModel =
            ViewModelProvider(
                this,
                MessageViewModelFactory(
                    messageRepo
                )
            )[MessageViewModel::class.java]
        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true

        binding.messagesRecyclerView.layoutManager = manager
        binding.messagesRecyclerView.adapter = adapter
        messageViewModel.messages.observe(this) {
            adapter.setMessages(it)
            if (it.isNotEmpty()) {
                manager.smoothScrollToPosition(binding.messagesRecyclerView, null, it.size - 1)
            }
        }
        messageViewModel.getMessages()

        binding.sendButton.setOnClickListener {

            val messageBody = binding.messageBox.text
            if (!messageBody.isNullOrBlank()) {
                messageViewModel.sendMessage(
                    Message(
                        messageBody.toString(),
                        auth.currentUser?.uid
                    )
                )
                binding.messageBox.setText("")
            }
        }*/
        binding.logoutButton.setOnClickListener {
            logout()
        }

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