package com.a2k.chatapp.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.a2k.chatapp.R
import com.a2k.chatapp.adapters.MessageAdapter
import com.a2k.chatapp.adapters.ProfileAdapter
import com.a2k.chatapp.databinding.ActivityMainBinding
import com.a2k.chatapp.models.Message
import com.a2k.chatapp.models.Profile
import com.a2k.chatapp.repository.MessageRepo
import com.a2k.chatapp.repository.ProfileRepo
import com.a2k.chatapp.setupUIWithNavigationListener
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
    val adapter = MessageAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUIWithNavigationListener(this, window, binding.toolbar) {
            finish()
        }
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setSupportActionBar(binding.toolbar)


        val chatId = intent.getStringExtra("chatId")
        val receiverProfile: Profile? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("receiverProfile", Profile::class.java)
            } else {
                intent.getParcelableExtra("receiverProfile")
            }

        title = (receiverProfile?.name)
        adapter.setReceiverInfo(receiverProfile)

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
                        messageBody =  messageBody.toString(),
                        senderId = auth.currentUser?.uid
                    )
                )
                binding.messageBox.setText("")
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
            R.id.logout -> {
                logout()
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