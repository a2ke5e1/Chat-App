package com.a2k.chatapp.screens

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.a2k.chatapp.adapters.MessageAdapter
import com.a2k.chatapp.adapters.ProfileAdapter
import com.a2k.chatapp.databinding.ActivityMainBinding
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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    val adapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val chatId = intent.getStringExtra("chatId")
        val receiverName = intent.getStringExtra("receiverName")
        val receiverUid = intent.getStringExtra("receiverUid")

        adapter.setReceiverInfo(receiverName, receiverUid)

        val messageRepo = MessageRepo(chatId!!)
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
        }
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