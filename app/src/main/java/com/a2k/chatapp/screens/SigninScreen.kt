package com.a2k.chatapp.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.a2k.chatapp.R
import com.a2k.chatapp.databinding.ActivitySigninScreenBinding
import com.a2k.chatapp.setupUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SigninScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySigninScreenBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivitySigninScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI(this, window, binding.toolbar)





        binding.noAccountBtn.setOnClickListener {
            // Move to Sign Up screen for user who does not have an account.
            val signupIntent = Intent(this, SignupScreen::class.java)
            startActivity(signupIntent)
        }


        binding.submitBtn.setOnClickListener {
            val email: String = binding.emailField.text.toString()
            val password: String = binding.passwordField.text.toString()

            binding.emailContainer.error = emailValidator(email)
            binding.passwordContainer.error = passwordValidator(password)

            val isFormValid = emailValidator(email) == null &&
                    passwordValidator(password) == null



            if (isFormValid) {

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser

                            if (user != null) {
                                // Move back to home screen

                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)
                                finish()

                            }

                        } else {

                            Log.d( "LOGIN_ERROR", task.exception?.message.toString())
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




    fun emailValidator(email: CharSequence?): String? {
        if (email.isNullOrEmpty()) {
            return getString(R.string.enter_email)
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return getString(R.string.enter_a_valid_email)
        }
        return null
    }

    fun passwordValidator(password: CharSequence?): String? {
        if (password.isNullOrEmpty()) {
            return getString(R.string.enter_password)
        }
        if (password.length < 8) {
            return getString(R.string.password_length_rule)
        }
        return null
    }




}