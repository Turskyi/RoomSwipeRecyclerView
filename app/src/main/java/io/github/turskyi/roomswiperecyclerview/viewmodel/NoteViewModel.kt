package io.github.turskyi.roomswiperecyclerview.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import io.github.turskyi.roomswiperecyclerview.model.NoteEntity
import io.github.turskyi.roomswiperecyclerview.data.NoteRepository

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository = NoteRepository(application)
    val allNotes: LiveData<List<NoteEntity>>
    init {
        allNotes = repository.allNotes
    }
    fun insert(note: NoteEntity?) {
        repository.insert(note)
    }

    fun update(note: NoteEntity?) {
        repository.update(note)
    }

    fun delete(note: NoteEntity?) {
        repository.delete(note)
    }

    fun deleteAllNotes() {
        repository.deleteAllNotes()
    }
}