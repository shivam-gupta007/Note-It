package com.app.noteit.feature_note.utils

import android.content.Context
import android.widget.Toast


fun toast(context: Context, text: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, length).show()
}