package com.nigdroid.login_and_sign_up

data class NoteItem(val title: String, val description: String, var noteId: String) {
    constructor() : this("", "", "")
}

