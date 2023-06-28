package com.sukses.jetnoteapp.data

import com.sukses.jetnoteapp.model.Note

class NoteDataSource {
    fun loadNotes(): List<Note> {
        return listOf(
            Note(title = "Monday", description = "Time for school"),
            Note(title = "Tuesday", description = "Time for school"),
            Note(title = "Wednesday", description = "Time for school"),
            Note(title = "Thursday", description = "Time for school"),
            Note(title = "Friday", description = "Time for school"),
            Note(title = "Saturday", description = "Time for Billiard"),
            Note(title = "Sunday", description = "Time for Gaming"),
            Note(title = "Good Day", description = "Time for sex"),
        )
    }
}