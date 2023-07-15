package com.a2k.chatapp.screens

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.a2k.chatapp.R
import com.a2k.chatapp.databinding.ActivityProfileCustomizeBinding
import com.a2k.chatapp.models.Profile
import com.a2k.chatapp.repository.ProfileRepo
import com.a2k.chatapp.setupUI
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class ProfileCustomizeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileCustomizeBinding
    private lateinit var auth: FirebaseAuth
    private var updatedPhotoUrl: Uri? = null
    private var userProfileRef: StorageReference? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            updatedPhotoUrl = uri
            Glide.with(this)
                .load(updatedPhotoUrl)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                       Log.d("Test", "failed")
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        val bitmap = resource.toBitmap()
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val bytes = baos.toByteArray()
                        val uploadTask = userProfileRef?.putBytes(bytes)
                        uploadTask?.addOnFailureListener {
                            Log.d("test_photo", it.stackTraceToString())
                        }?.addOnSuccessListener { taskSnapshot ->
                            Toast.makeText(baseContext, "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
                        }
                        return false
                    }

                })
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.profileAvatar)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
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
        userProfileRef = Firebase.storage.reference.child("user_profiles").child(uid)
        binding.nameField.setText(name)
        Glide.with(this)
            .load(userProfileRef)
            .placeholder(R.drawable.ic_launcher_foreground)
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
                        uid = uid
                    ), uid
                ).addOnSuccessListener {
                    Toast.makeText(baseContext, "Updated Successfully", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.uploadBtn.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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