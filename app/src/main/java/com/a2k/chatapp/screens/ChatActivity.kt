package com.a2k.chatapp.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.a2k.chatapp.R
import com.a2k.chatapp.adapters.MessageAdapter
import com.a2k.chatapp.databinding.ActivityChatBinding
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.models.Profile
import com.a2k.chatapp.repository.MessageRepo
import com.a2k.chatapp.setupUI
import com.a2k.chatapp.viewmodel.MessageViewModel
import com.a2k.chatapp.viewmodel.MessageViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    var editingMode = false
    var editingMessageId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(this, window,binding.appBarLayout, false)
        setSupportActionBar(binding.toolbar)
        WindowCompat.setDecorFitsSystemWindows(window, true)


        val chatId = intent.getStringExtra("chatId")
        val receiverProfile: Profile? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("receiverProfile", Profile::class.java)
            } else {
                intent.getParcelableExtra("receiverProfile")
            }

        title = (receiverProfile?.name)

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


        val adapter = MessageAdapter(this, { documentId ->
            messageViewModel.deleteMessage(documentId)
        }, { documentId , messageBody ->
            editingMode = true
            editingMessageId = documentId
            binding.messageBox.setText(messageBody)
        })
        adapter.setReceiverInfo(receiverProfile)


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
            if ( editingMode) {
                if (!messageBody.isNullOrBlank()) {
                    messageViewModel.updateMessage(
                        messageBody.toString(), editingMessageId!!
                    )
                    binding.messageBox.setText("")
                    editingMode = false
                }
            } else {
                if (!messageBody.isNullOrBlank()) {
                    messageViewModel.sendMessage(
                        Message(
                            messageBody = messageBody.toString(),
                            senderId = auth.currentUser?.uid
                        )
                    )
                    binding.messageBox.setText("")
                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.appbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.logout -> {
                logout()
                true
            }
            R.id.edit_profile -> {
                startActivity(Intent(this, ProfileCustomizeActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
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