package com.app.noteit.feature_note.presentation.secure_notes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.app.noteit.feature_note.utils.Constants
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    navController: NavHostController,
    noteColor: Int,
    noteId: Int
) {
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


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(all = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        LottieAnimationItem(
            modifier = Modifier.size(size = 250.dp),
            animationUrl = Constants.LOCK_ANIMATION_URL,
            iterateForever = false
        )


        Spacer(modifier = Modifier.height(height = 10.dp))

        Text(
            text = if (passcode.value.isEmpty()) "Enter your passcode" else "Verify your passcode",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
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
                    } else {
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
                    if (it.isBlank()) {
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
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )
}
