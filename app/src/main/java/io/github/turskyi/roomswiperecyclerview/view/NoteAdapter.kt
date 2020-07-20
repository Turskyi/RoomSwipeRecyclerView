package io.github.turskyi.roomswiperecyclerview.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.turskyi.roomswiperecyclerview.R
import io.github.turskyi.roomswiperecyclerview.data.NoteEntity
import io.github.turskyi.roomswiperecyclerview.view.NoteAdapter.NoteHolder
import kotlinx.android.synthetic.main.item_note.view.*

class NoteAdapter : RecyclerView.Adapter<NoteHolder>() {
    var notes: List<NoteEntity>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val currentNote = notes?.get(position)
        currentNote?.run {
            holder.textViewTitle.text = title
            holder.textViewDescription.text = description
            holder.textViewPriority.text = priority.toString()
        }
    }

    override fun getItemCount() = notes?.size ?: 0

    class NoteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.text_view_title
        val textViewDescription: TextView = itemView.text_view_description
        val textViewPriority: TextView = itemView.text_view_priority
    }
}