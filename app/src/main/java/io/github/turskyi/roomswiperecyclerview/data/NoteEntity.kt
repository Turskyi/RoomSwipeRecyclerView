package io.github.turskyi.roomswiperecyclerview.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.turskyi.roomswiperecyclerview.data.NoteEntity.Companion.TABLE_NOTES

@Entity(tableName = TABLE_NOTES)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_ID) var id: Int,
    @ColumnInfo(name = COLUMN_TITLE) val title: String,
    @ColumnInfo(name = COLUMN_DESCRIPTION) val description: String,
    @ColumnInfo(name = COLUMN_PRIORITY) val priority: Int
) {
    constructor( title: String, description: String, priority: Int) :
            this(0, title, description, priority)

    companion object {
        const val TABLE_NOTES = "note_table"
        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PRIORITY = "priority"
    }
}