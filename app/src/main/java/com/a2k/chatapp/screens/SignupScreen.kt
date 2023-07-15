package com.a2k.chatapp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.MenuItem
import android.widget.Toast
import com.a2k.chatapp.R
import com.a2k.chatapp.databinding.ActivitySignupScreenBinding
import com.a2k.chatapp.models.Profile
import com.a2k.chatapp.repository.ProfileRepo
import com.a2k.chatapp.setupUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class SignupScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySignupScreenBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivitySignupScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(this, window,binding.appBarLayout)
        setSupportActionBar(binding.toolbar)

        binding.alreadyRegisterBtn.setOnClickListener {
            // Goes back to sign in screen
            finish()
        }

        binding.submitBtn.setOnClickListener {
            val name: String = binding.nameField.text.toString().trim()
            val email: String = binding.emailField.text.toString().trim()
            val password: String = binding.passwordField.text.toString()
            val confirmPassword: String = binding.confirmPasswordField.text.toString()

            binding.nameContainer.error = nameValidator(name)
            binding.emailContainer.error = emailValidator(email)
            binding.passwordContainer.error = passwordValidator(password)
            binding.confirmPasswordContainer.error =
                confirmPasswordValidator(confirmPassword, password)

            val isFormValid = nameValidator(name) == null &&
                    emailValidator(email) == null &&
                    passwordValidator(password) == null &&
                    confirmPasswordValidator(password, confirmPassword) == null

            val profileDetails = UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()

            if (isFormValid) {

                auth.createUserWithEmailAndPassword(email, confirmPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            user?.updateProfile(profileDetails)
                            ProfileRepo().updateProfile(
                                Profile(
                                    name = name,
                                    email = email,
                                    photoUrl = null,
                                    uid = user?.uid
                                ),
                                user!!.uid
                            )

                            if (user != null) {
                                Toast.makeText(baseContext, user?.uid, Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            }

                        } else {


                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()


                        }

                    }

            }


        }

    }

    /** Checks if name is valid or not. */
    fun nameValidator(name: CharSequence?): String? {
        if (name.isNullOrEmpty()) {
            return getString(R.string.enter_a_name)
        }
        return null
    }

    /** Checks if given email is a valid email or not using
     *  Pattern matcher to match current email with email pattern
     *  provided by Pattern Class.
     */
    fun emailValidator(email: CharSequence?): String? {
        if (email.isNullOrEmpty()) {
            return getString(R.string.enter_email)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return getString(R.string.enter_a_valid_email)
        }
        return null
    }

    /** Password Validator validates current password with following rules:
     *  1. Password should be 8 characters long
     *  2. Password atleast contains a lower case letter, uppercase letter and a digit.
     *  3. Password must contain atleast one special character. */
    fun passwordValidator(password: CharSequence?): String? {
        if (password.isNullOrEmpty()) {
            return getString(R.string.enter_password)
        }
        val passwordPattern = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$"
        )
        if (!passwordPattern.matcher(password).matches()) {
            return getString(R.string.password_rules)
        }
        return null
    }

    /** It checks if the both password and otherPassword is same or not */
    fun confirmPasswordValidator(password: CharSequence?, otherPassword: CharSequence?): String? {
        if (password.isNullOrEmpty()) {
            return getString(R.string.enter_password)
        }
        if (!password.equals(otherPassword)) {
            return getString(R.string.password_does_not_match)
        }
        return null
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