package com.app.noteit.data.model

import com.app.noteit.domain.model.Note

fun NoteEntity.toNote() : Note {
    return Note(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        color = backgroundColor,
        isPinned = isPinned,
        isProtected = isProtected
    )
}

fun Note.toNoteEntity() : NoteEntity {
    return NoteEntity(
        id = id,
        title = title,
        content = content,
        timestamp = timestamp,
        backgroundColor = color,
        isPinned = isPinned,
        isProtected = isProtected
    )
}

fun List<NoteEntity>.toNoteList() : List<Note>{
    return this.map {
        it.toNote()
    }
}