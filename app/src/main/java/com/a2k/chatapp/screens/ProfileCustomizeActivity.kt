package com.a2k.chatapp.screens

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.a2k.chatapp.R
import com.a2k.chatapp.databinding.ActivityProfileCustomizeBinding
import com.a2k.chatapp.models.Profile
import com.a2k.chatapp.repository.ProfileRepo
import com.a2k.chatapp.setupUI
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

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

        val uid = auth.currentUser?.uid
        val name = auth.currentUser?.displayName
        val email = auth.currentUser?.email

        val profileRepo = ProfileRepo()
        if (uid == null) {
            return
        }
        binding.nameField.setText(name)
        Glide.with(this)
            .load(auth.currentUser?.photoUrl)
            .error(R.drawable.ic_launcher_foreground)
            .into(binding.profileAvatar)

        binding.submitBtn.setOnClickListener {

            val updatedName = binding.nameField.text.toString().trim()

            binding.nameContainer.error = nameValidator(updatedName)

            val isFormValid = nameValidator(name) == null && updatedName != name

            if (isFormValid) {
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(updatedName)
                    .build()
                auth.currentUser?.updateProfile(profileChangeRequest)
                profileRepo.updateProfile(
                    Profile(
                        name = updatedName,
                        email = email,
                        photoUrl = null,
                        uid = uid
                    ), uid
                ).addOnSuccessListener {
                    Toast.makeText(baseContext, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
            }

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

    /** Checks if name is valid or not. */
    fun nameValidator(name: CharSequence?): String? {
        if (name.isNullOrEmpty()) {
            return getString(R.string.enter_a_name)
        }
        return null
    }


}