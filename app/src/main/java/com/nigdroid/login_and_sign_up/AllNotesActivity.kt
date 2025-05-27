package com.nigdroid.login_and_sign_up

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nigdroid.login_and_sign_up.databinding.ActivityAllNotesBinding

class AllNotesActivity : AppCompatActivity() {

    private val binding: ActivityAllNotesBinding by lazy {
        ActivityAllNotesBinding.inflate(layoutInflater)
    }


    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        recyclerView=binding.noteRecyclerView
        recyclerView.layoutManager= LinearLayoutManager(this)


//      inititalise firebase database reference

        databaseReference= FirebaseDatabase.getInstance().reference
        auth= FirebaseAuth.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let{
            user ->
            val noteReference: DatabaseReference=databaseReference.child("users").child(user.uid.toString()).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        val noteList: MutableList<NoteItem> = mutableListOf()
                    for (noteSnapshot in snapshot.children){
                        val note : NoteItem? =noteSnapshot.getValue(NoteItem::class.java)
                        note?.let {
                            noteList.add(it)

                        }
                    }
                    var adapter= NoteAdapter(noteList)
                    recyclerView.adapter=adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }
}