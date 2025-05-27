package com.nigdroid.login_and_sign_up

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.nigdroid.login_and_sign_up.databinding.ActivityMainBinding
import com.nigdroid.login_and_sign_up.databinding.ActivitySignUpBinding

class MainActivity : AppCompatActivity() {

private val binding: ActivityMainBinding by lazy{
    ActivityMainBinding.inflate(layoutInflater)
}
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

//      Log out after clicking the sign out button

binding.signOutButton.setOnClickListener {
    signOutUser()
    if (auth.currentUser == null) {
        navigateToLogin()
    }
}
        binding.newNote.setOnClickListener {
            startActivity(Intent(this, AddnoteActivity::class.java))
        }
        binding.openNote.setOnClickListener {
            startActivity(Intent(this, AllNotesActivity::class.java))
        }

    }


//    function for signing out

    private fun signOutUser() {
        auth.signOut() //  this code is to sign out from Firebase

        // Navigate back to LoginActivity
        navigateToLogin()

        Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        // Add flags to clear the back stack and prevent going back to MainActivity after sign-out
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            navigateToLogin()
        }
    }
}


