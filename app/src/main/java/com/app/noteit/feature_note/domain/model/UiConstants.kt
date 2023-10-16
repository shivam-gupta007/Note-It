package com.app.noteit.feature_note.domain.model

sealed class EditNoteSheetActionType(value: String){
    object DeleteNote : EditNoteSheetActionType("delete_note")
    object CopyNote : EditNoteSheetActionType("copy_note")
    object ShareNote : EditNoteSheetActionType("share_note")
}