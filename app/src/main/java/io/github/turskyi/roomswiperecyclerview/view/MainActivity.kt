package io.github.turskyi.roomswiperecyclerview.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.turskyi.roomswiperecyclerview.R
import io.github.turskyi.roomswiperecyclerview.model.NoteEntity
import io.github.turskyi.roomswiperecyclerview.viewmodel.NoteViewModel
import io.github.turskyi.roomswiperecyclerview.viewmodel.NoteViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    companion object {
        const val ADD_NOTE_REQUEST = 1
        const val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteViewModel = ViewModelProvider(this, NoteViewModelFactory(this.application))
            .get(NoteViewModel::class.java)

        buttonAddNote.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        val adapter = NoteAdapter()
        recyclerView.adapter = adapter
        noteViewModel.allNotes.observe(this, Observer<List<NoteEntity>?> {
            /* update RecyclerView */
            adapter.submitList(it)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            )= false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(recyclerView)

        adapter.onItemClickListener =  {
                val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, it.id)
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, it.title)
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, it.description)
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, it.priority)
                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
            val note = title?.let { noteTitle: String ->
                description?.let { description ->
                    priority?.let { priority ->
                        NoteEntity(
                            noteTitle,
                            description,
                            priority
                        )
                    }
                }
            }
            noteViewModel.insert(note)
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            val id: Int? = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }
            val title: String? = data?.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description: String? =
                data?.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority: Int? = data?.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)
            val note = title?.let { noteTitle: String ->
                description?.let { description ->
                    priority?.let { priority ->
                        NoteEntity(
                            noteTitle,
                            description,
                            priority
                        )
                    }
                }
            }
            id?.let{ note?.id = it }
            noteViewModel.update(note)
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_all_notes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}