package io.github.turskyi.roomswiperecyclerview.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.turskyi.roomswiperecyclerview.AddNoteActivity
import io.github.turskyi.roomswiperecyclerview.R
import io.github.turskyi.roomswiperecyclerview.data.NoteEntity
import io.github.turskyi.roomswiperecyclerview.viewmodel.NoteViewModel
import io.github.turskyi.roomswiperecyclerview.viewmodel.NoteViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        const val ADD_NOTE_REQUEST = 1
    }

    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(this.application))
            .get(NoteViewModel::class.java)

        buttonAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter
        noteViewModel.allNotes.observe(this, Observer<List<NoteEntity>?> {
            /* update RecyclerView */
            adapter.notes = it
        })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AddNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddNoteActivity.EXTRA_PRIORITY, 1)
            val note = title?.let { noteTitle: String ->
                description?.let { description ->
                    priority?.let { priority ->
                        NoteEntity(noteTitle, description, priority)
                    }
                }
            }
            noteViewModel.insert(note)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }
}