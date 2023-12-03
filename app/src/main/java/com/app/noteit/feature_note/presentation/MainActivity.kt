package com.app.noteit.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.noteit.feature_note.presentation.add_edit_notes.components.AddEditNoteScreen
import com.app.noteit.feature_note.presentation.notes.components.NotesScreen
import com.app.noteit.feature_note.presentation.secure_notes.AuthenticationScreen
import com.app.noteit.feature_note.presentation.util.Screen
import com.app.noteit.ui.theme.NoteItTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteItTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route,
                    ) {
                        composable(route = Screen.NotesScreen.route, enterTransition = { fadeIn() }, popEnterTransition = { fadeIn() }, exitTransition = { fadeOut() }, popExitTransition = { fadeOut() }) {
                            NotesScreen(onAddNoteAction = {
                                navController.navigate(Screen.AddEditNotesScreen.route)
                            }, onNoteClicked = { route ->
                                navController.navigate(route)
                            })
                        }

                        composable(route = Screen.AddEditNotesScreen.route + "?noteId={noteId}&noteColor={noteColor}", arguments = listOf(navArgument(
                            name = "noteId"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        }, navArgument(
                            name = "noteColor"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        }), enterTransition = { scaleIn() }, popEnterTransition = { scaleIn() }, exitTransition = { fadeOut() }, popExitTransition = { fadeOut() }) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                noteColor = color,
                                onNoteSaved = {
                                    navController.popBackStack()
                                },
                                onNoteUnsaved = {
                                    navController.popBackStack()
                                },
                                onLockNoteClicked = {
                                    navController.navigate(route = Screen.AuthenticationScreen.route)
                                }
                            )
                        }

                        composable(route = Screen.AuthenticationScreen.route + "?noteId={noteId}&noteColor={noteColor}", arguments = listOf(navArgument(
                            name = "noteId"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        }, navArgument(
                            name = "noteColor"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        }), enterTransition = {
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
                                )
                            )
                        }, popEnterTransition = {
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow
                                )
                            )
                        }, exitTransition = { fadeOut() }, popExitTransition = { fadeOut() }) {
                            val noteColor = it.arguments?.getInt("noteColor") ?: -1
                            val noteId = it.arguments?.getInt("noteId") ?: -1

                            AuthenticationScreen(
                                onPasscodeSaved = {
                                    navController.navigateUp()
                                },
                                onPasscodeConfirmed = {
                                    navController.navigate(Screen.AddEditNotesScreen.route + "?noteId=${noteId}&noteColor=${noteColor}") {
                                        popUpTo(Screen.NotesScreen.route)
                                    }
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}

