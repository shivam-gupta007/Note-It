package com.app.noteit.feature_note.presentation.secure_notes

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.noteit.feature_note.data.data_source.preferences.PasscodeDataStore
import com.app.noteit.feature_note.presentation.LottieAnimationItem
import com.app.noteit.feature_note.presentation.util.Screen
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    navController: NavHostController,
    noteColor: Int,
    noteId: Int
) {
    val TAG = "AuthenticationScreen"
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dataStore = PasscodeDataStore(context = context)
    val passcode = dataStore.getPin.collectAsState(initial = "")

    var passcodeDigit1 by remember { mutableStateOf(value = "") }
    var passcodeDigit2 by remember { mutableStateOf(value = "") }
    var passcodeDigit3 by remember { mutableStateOf(value = "") }
    var passcodeDigit4 by remember { mutableStateOf(value = "") }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester1.requestFocus()
    }

    Log.i(TAG, "noteId: $noteId noteColor: $noteColor")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LottieAnimationItem(
            modifier = Modifier.size(size = 250.dp),
            animationUrl = "https://assets10.lottiefiles.com/packages/lf20_bmfghkq7.json",
            iterateForever = false
        )

        Spacer(modifier = Modifier.height(height = 10.dp))

        Text(
            text = if (passcode.value.isEmpty()) "Enter your passcode" else "Verify your passcode",
            fontSize = MaterialTheme.typography.h6.fontSize
        )

        Spacer(modifier = Modifier.height(height = 15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PasscodeTextField(
                modifier = Modifier.focusRequester(focusRequester1),
                value = passcodeDigit1,
                onValueChange = {
                    passcodeDigit1 = it
                    if (it.isNotBlank()) {
                        focusRequester2.requestFocus()
                    }
                }
            )

            PasscodeTextField(
                modifier = Modifier.focusRequester(focusRequester2),
                value = passcodeDigit2,
                onValueChange = {
                    passcodeDigit2 = it
                    if (it.isNotBlank()) {
                        focusRequester3.requestFocus()
                    } else{
                        focusRequester1.requestFocus()
                    }
                }
            )

            PasscodeTextField(
                modifier = Modifier.focusRequester(focusRequester3),
                value = passcodeDigit3,
                onValueChange = {
                    passcodeDigit3 = it
                    if (it.isNotBlank()) {
                        focusRequester4.requestFocus()
                    } else {
                        focusRequester2.requestFocus()
                    }
                }
            )

            PasscodeTextField(
                modifier = Modifier
                    .focusRequester(focusRequester4),
                value = passcodeDigit4,
                onValueChange = {
                    passcodeDigit4 = it
                    if(it.isBlank()){
                        focusRequester3.requestFocus()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(height = 10.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            onClick = {
                val enteredPasscode =
                    passcodeDigit1 + passcodeDigit2 + passcodeDigit3 + passcodeDigit4
                if (passcode.value.isBlank()) {
                    coroutineScope.launch {
                        dataStore.setPin(pin = enteredPasscode)
                    }
                    navController.navigateUp()
                } else {
                    Toast.makeText(
                        context,
                        if (passcode.value == enteredPasscode) {
                            navController.navigate(Screen.AddEditNotesScreen.route + "?noteId=${noteId}&noteColor=${noteColor}") {
                                popUpTo(Screen.NotesScreen.route)
                            }
                            "Passcode confirmed"
                        } else {
                            "Invalid Passcode"
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        ) {
            Text(text = if (passcode.value.isEmpty()) "SAVE" else "CONFIRM")
        }
    }
}

@Composable
fun PasscodeTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = modifier
            .width(width = 70.dp)
            .padding(all = 8.dp),
        value = value,
        onValueChange = {
            onValueChange(
                if (it.isNotBlank()) it[0].toString() else ""
            )
        },
        minLines = 1,
        maxLines = 1,
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.h6.fontSize
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
