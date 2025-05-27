package com.nigdroid.login_and_sign_up

import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.nigdroid.login_and_sign_up.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

//      this is to  intitialise firebase auth
        auth= FirebaseAuth.getInstance()

    binding.signIn.setOnClickListener {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

       binding.register.setOnClickListener {


           val email=binding.email.text.toString()
           val usrname=binding.usrname.text.toString()
           val password=binding.password.text.toString()
           var repassword=binding.rePassword.text.toString()


           if(email.isEmpty()|| usrname.isEmpty()|| password.isEmpty()||repassword.isEmpty()){
               Toast.makeText(this,"Please fill all the details", Toast.LENGTH_SHORT).show()
           }
           else if (repassword!=password){
               Toast.makeText(this,"Repeat password must be same", Toast.LENGTH_SHORT).show()
           }

//            this is the real step to add authenticating data to firebase

            else{
                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this) {task->
                        if(task.isSuccessful){
                                Toast.makeText(this,"Registration successful", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        else{
                            Toast.makeText(this,"Registration Failed :${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

           }

       }


 }

    }


