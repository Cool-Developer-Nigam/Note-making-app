package com.nigdroid.login_and_sign_up

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nigdroid.login_and_sign_up.databinding.NotesItemBinding

class NoteAdapter(private val notes: List<NoteItem>,private val itemClickListener : OnItemClickListener):
    RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

        interface OnItemClickListener{
            fun onDeleteClick(noteId: String)
            fun onEditClick(noteId: String, tittle: String,description: String)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NoteAdapter.NoteViewHolder {
        val binding: NotesItemBinding =
            NotesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        val note: NoteItem = notes[position]
        holder.bind(note)
        holder.binding.editBtn.setOnClickListener {

            itemClickListener.onEditClick(note.noteId,note.title,note.description)

        }
        holder.binding.deleteBtn.setOnClickListener {

            itemClickListener.onDeleteClick(note.noteId)


        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    class NoteViewHolder(val binding: NotesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: NoteItem){
            binding.titleTxt.text=note.title
            binding.desTxt.text=note.description
        }
    }


}