package io.github.turskyi.roomswiperecyclerview.data

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.github.turskyi.roomswiperecyclerview.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    companion object {
        private var instance: NoteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): NoteDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java, "note_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()
            }
            return instance
        }

        private val roomCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(instance!!).execute()
            }
        }
    }
    abstract fun noteDao(): NoteDao
    private class PopulateDbAsyncTask internal constructor(db: NoteDatabase) :
        AsyncTask<Void?, Void?, Void?>() {
        private val noteDao: NoteDao = db.noteDao()
        override fun doInBackground(vararg voids: Void?): Void? {
            noteDao.insert(
                NoteEntity(
                    "Title 1",
                    "Description 1",
                    1
                )
            )
            noteDao.insert(
                NoteEntity(
                    "Title 2",
                    "Description 2",
                    2
                )
            )
            noteDao.insert(
                NoteEntity(
                    "Title 3",
                    "Description 3",
                    3
                )
            )
            return null
        }
    }
}