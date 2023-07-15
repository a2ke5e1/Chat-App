package com.a2k.chatapp.screens

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.a2k.chatapp.R
import com.a2k.chatapp.adapters.ProfileAdapter
import com.a2k.chatapp.databinding.ActivityHomeBinding
import com.a2k.chatapp.repository.ProfileRepo
import com.a2k.chatapp.setupUI
import com.a2k.chatapp.viewmodel.ProfilesViewModel
import com.a2k.chatapp.viewmodel.ProfilesViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    val adapter = ProfileAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityHomeBinding.inflate(layoutInflater)
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