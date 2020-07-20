package io.github.turskyi.roomswiperecyclerview.data

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {
    private val noteDao: NoteDao
    val allNotes: LiveData<List<NoteEntity>>

    init {
        val database = NoteDatabase.getInstance(application)!!
        noteDao = database.noteDao()
        allNotes = noteDao.allNotes
    }

    fun insert(note: NoteEntity?) {
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: NoteEntity?) {
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: NoteEntity?) {
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes() {
        DeleteAllNotesAsyncTask(noteDao).execute()
    }

    private class InsertNoteAsyncTask(private val noteDao: NoteDao?) :
        AsyncTask<NoteEntity?, Void?, Void?>() {
        override fun doInBackground(vararg notes: NoteEntity?): Void? {
            noteDao?.insert(notes[0])
            return null
        }
    }

    private class UpdateNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<NoteEntity?, Void?, Void?>() {
        override fun doInBackground(vararg notes: NoteEntity?): Void? {
            noteDao.update(notes[0])
            return null
        }
    }

    private class DeleteNoteAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<NoteEntity?, Void?, Void?>() {
        override fun doInBackground(vararg notes: NoteEntity?): Void? {
            noteDao.delete(notes[0])
            return null
        }
    }

    private class DeleteAllNotesAsyncTask(private val noteDao: NoteDao) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg voids: Void?): Void? {
            noteDao.deleteAllNotes()
            return null
        }
    }
}