package com.nigdroid.login_and_sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
import com.nigdroid.login_and_sign_up.databinding.DialogUpdateNotesBinding

class AllNotesActivity : AppCompatActivity() , NoteAdapter.OnItemClickListener {

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


        recyclerView = binding.noteRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)


//      inititalise firebase database reference

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let { user ->
            val noteReference: DatabaseReference =
                databaseReference.child("users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val noteList: MutableList<NoteItem> = mutableListOf()
                    for (noteSnapshot in snapshot.children) {
                        val note: NoteItem? = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let { currentNote ->
                            currentNote.noteId =
                                noteSnapshot.key ?: "" // <<< --- FIX: ASSIGN THE FIREBASE KEY
                            noteList.add(currentNote)
//                            noteList.add(it)
                        }
                        noteList.reverse()
                    }
                    var adapter = NoteAdapter(noteList, this@AllNotesActivity)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        }

    }

    override fun onDeleteClick(noteId: String) {

        val currentUser: FirebaseUser? = auth.currentUser
        currentUser?.let { user ->
            val noteReference: DatabaseReference =
                databaseReference.child("users").child(user.uid).child("notes")
            noteReference.child(noteId).removeValue()
        }
    }

    override fun onEditClick(noteId: String, currentTitle: String, currentDescription: String) {

        val dialogBinding = DialogUpdateNotesBinding.inflate(LayoutInflater.from(this))
        val dialog = AlertDialog.Builder(this).setView(dialogBinding.root)
            .setTitle("Update notes")
            .setPositiveButton("Update") { dialog,_ ->

                val newTitle: String = dialogBinding.updateTitle.text.toString()
                val newDes: String = dialogBinding.updateDes.text.toString()
                updateNoteDatabase(noteId, newTitle, newDes)
                dialog.dismiss()}
            .setNegativeButton("Cancel ") {
                dialog,_->
                dialog.dismiss()
            }
            .create()

        dialogBinding.updateTitle.setText(currentTitle)
        dialogBinding.updateDes.setText(currentDescription)

        dialog.show()

    }


    private fun AllNotesActivity.updateNoteDatabase(
        noteId: String,
        newTitle: String,
        newDescription: String
    ) {
        val currentUser: FirebaseUser? = auth.currentUser

        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            val updateNote = NoteItem(newTitle, newDescription, noteId)
            noteReference.child(noteId).setValue(updateNote)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note Updated Sucessfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}