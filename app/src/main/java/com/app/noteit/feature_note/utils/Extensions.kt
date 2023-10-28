package com.app.noteit.feature_note.utils

import android.content.Context
import android.content.Intent

fun Context.shareNote(note: String){
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_TEXT,note)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(
        sendIntent,"Share notes"
    )

    startActivity(shareIntent)
}