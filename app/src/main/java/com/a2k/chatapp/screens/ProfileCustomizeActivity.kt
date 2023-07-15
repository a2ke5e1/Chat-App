package com.a2k.chatapp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.a2k.chatapp.R
import com.a2k.chatapp.databinding.ActivityHomeBinding
import com.a2k.chatapp.databinding.ActivityProfileCustomizeBinding
import com.a2k.chatapp.setupUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileCustomizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileCustomizeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityProfileCustomizeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(this, window, binding.appBarLayout, false)
        setSupportActionBar(binding.toolbar)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}