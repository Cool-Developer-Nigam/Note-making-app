package com.nigdroid.login_and_sign_up

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nigdroid.login_and_sign_up.databinding.ActivityAddnoteBinding

class AddnoteActivity : AppCompatActivity() {

    private val binding: ActivityAddnoteBinding by lazy {
        ActivityAddnoteBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        databaseReference= FirebaseDatabase.getInstance().reference

        auth= FirebaseAuth.getInstance()

        binding.saveBtn.setOnClickListener {

            val title=binding.edtTxtTitle.text.toString()
            val description=binding.edtTxtDes.text.toString()

            if(title.isEmpty() && description.isEmpty()){
                Toast.makeText(this, "Fill both fields", Toast.LENGTH_SHORT).show()
            }
            else {

                val currentUser=auth.currentUser
                currentUser?.let { user ->

//                    generate a new key
                    val noteKey: String? =databaseReference.child("users").child(user.uid).child("notes").push().key

//                    note Item instance
                    val noteItem= NoteItem(title,description,noteKey?:"")

                    if(noteKey!=null){
//                        add notes to user notes

                        databaseReference.child("users").child(user.uid).child("notes").child(noteKey).setValue(noteItem)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    Toast.makeText(this, "Note save successfully", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else{
                                    Toast.makeText(this, "Fail to save", Toast.LENGTH_SHORT).show()
                                }
                            }

                    }

                }
            }

        }

    }
}