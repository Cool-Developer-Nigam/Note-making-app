package com.nigdroid.login_and_sign_up

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nigdroid.login_and_sign_up.databinding.ActivityForgotPasswordBinding // Import your ViewBinding class

class ForgotPasswordActivity : AppCompatActivity() {

    // Declare ViewBinding variable
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // For edge-to-edge display

        // Inflate the layout using ViewBinding
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Set click listener for the "Send Reset Email" button
        binding.btnSendResetEmail.setOnClickListener {
            val email = binding.etEmailForgot.text.toString().trim()
            if (validateEmail(email)) {
                sendPasswordResetLink(email)
            }
        }

        // Set click listener for the "Back to Login" text
        binding.tvBackToLogin.setOnClickListener {
            // Navigate back to LoginActivity
            // You might want to finish this activity or use specific intent flags
            // depending on your navigation flow.
            val intent = Intent(this, LoginActivity::class.java) // Assuming LoginActivity is your login screen
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish() // Finish ForgotPasswordActivity
        }
    }

    private fun validateEmail(email: String): Boolean {
        if (email.isEmpty()) {
            binding.tilEmailForgot.error = "Email address cannot be empty."
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmailForgot.error = "Please enter a valid email address."
            return false
        }
        // Clear error if validation passes
        binding.tilEmailForgot.error = null
        return true
    }

    private fun sendPasswordResetLink(email: String) {
        showLoading(true) // Show progress bar

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                showLoading(false) // Hide progress bar
                if (task.isSuccessful) {
                    Log.d("ForgotPasswordActivity", "Password reset email sent successfully to $email")
                    Toast.makeText(
                        this,
                        "Password reset email sent. Please check your inbox (and spam folder).",
                        Toast.LENGTH_LONG
                    ).show()
                    // Optionally, you could automatically navigate back to login after a delay
                    // or let the user do it via "Back to Login".
                } else {
                    Log.e("ForgotPasswordActivity", "Failed to send password reset email", task.exception)
                    Toast.makeText(
                        this,
                        "Failed to send reset email. ${task.exception?.message ?: "Unknown error. Check logs."}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarForgotPassword.visibility = View.VISIBLE
            binding.btnSendResetEmail.isEnabled = false // Disable button while loading
        } else {
            binding.progressBarForgotPassword.visibility = View.GONE
            binding.btnSendResetEmail.isEnabled = true  // Re-enable button
        }
    }
}
