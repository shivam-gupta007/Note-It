package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.app.noteit.ui.theme.DialogBackgroundColor

@Composable
fun AuthDialog(
    modifier: Modifier = Modifier
) {
    var openDialog by remember { mutableStateOf(value = true) }
    var pinText by remember { mutableStateOf(value = "") }
    var pinTextList = arrayListOf("", "", "", "")

    if (openDialog) {
        Dialog(
            onDismissRequest = { openDialog = !openDialog }
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(size = 12.dp))
                    .background(color = DialogBackgroundColor)
                    .padding(all = 12.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(size = 40.dp),
                    imageVector = Icons.Outlined.LockOpen,
                    contentDescription = "Lock open icon",
                    tint = MaterialTheme.colors.primary
                )

                Spacer(modifier = Modifier.height(height = 10.dp))

                Text(
                    text = "Protect your note with a 4 digit pin",
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colors.onPrimary
                )

                Spacer(modifier = Modifier.height(height = 20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(4) { index ->
                        OutlinedTextField(
                            modifier = Modifier
                                .width(width = 55.dp)
                                .height(height = 55.dp),
                            value = pinTextList[index],
                            onValueChange = { text ->
                                pinTextList.set(
                                    index = index,
                                    element = if (text.isNotBlank()) {
                                        text[0].toString()
                                    } else {
                                        ""
                                    }
                                )
                            },
                            shape = RoundedCornerShape(size = 120.dp),
                            singleLine = true,
                            maxLines = 1,
                            minLines = 1,
                            textStyle = TextStyle(fontSize = 20.sp, textAlign = TextAlign.Center),
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                            visualTransformation = PasswordVisualTransformation(),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(height = 15.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        openDialog = !openDialog
                    }
                ) {
                    Text(text = "Save")
                }
            }
        }
    }
}

@Preview
@Composable
fun AuthDialogPreview() {
    AuthDialog()
}