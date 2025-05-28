package com.nigdroid.login_and_sign_up

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.nigdroid.login_and_sign_up.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

//    this code is written so that if you have done login then it will not show you the sign in screen

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser

        if(currentUser!= null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        binding.signUp.setOnClickListener {
    startActivity(Intent(this, SignUpActivity::class.java))
    finish()
}
        binding.login.setOnClickListener {
            val usrname=binding.usrname.text.toString()
            val password=binding.password.text.toString()
if(usrname.isEmpty()||password.isEmpty()){
    Toast.makeText(this,"Please fill all the details", Toast.LENGTH_SHORT).show()
}

//This is to check whether the users email and password are as entered during sign up or not

else{
    auth.signInWithEmailAndPassword(usrname,password)
        .addOnCompleteListener {
            task->if (task.isSuccessful){
            Toast.makeText(this,"sign in successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
            finish()
            }
            else{
            Toast.makeText(this,"Sign in failed : ${task.exception?.message}", Toast.LENGTH_SHORT).show()
        }

        }
}

        }


    }
}