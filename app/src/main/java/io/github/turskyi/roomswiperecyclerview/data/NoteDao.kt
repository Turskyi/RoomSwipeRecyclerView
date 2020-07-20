package io.github.turskyi.roomswiperecyclerview.data

import androidx.lifecycle.LiveData
import androidx.room.*
import io.github.turskyi.roomswiperecyclerview.data.NoteEntity.Companion.COLUMN_PRIORITY
import io.github.turskyi.roomswiperecyclerview.data.NoteEntity.Companion.TABLE_NOTES

@Dao
interface NoteDao {
    @Insert
    fun insert(note: NoteEntity?)

    @Update
    fun update(note: NoteEntity?)

    @Delete
    fun delete(note: NoteEntity?)

    @Query("DELETE FROM $TABLE_NOTES")
    fun deleteAllNotes()

    @get:Query("SELECT * FROM $TABLE_NOTES ORDER BY $COLUMN_PRIORITY DESC")
    val allNotes: LiveData<List<NoteEntity>>
}