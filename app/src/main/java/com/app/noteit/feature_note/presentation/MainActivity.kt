package com.app.noteit.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
                        composable(
                            route = Screen.NotesScreen.route,
                            enterTransition = { fadeIn() },
                            popEnterTransition = { fadeIn() },
                            exitTransition = { fadeOut() },
                            popExitTransition = { fadeOut() }
                        ) {
                            NotesScreen(navController = navController)
                        }

                        composable(
                            route = Screen.AddEditNotesScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            ),
                            enterTransition = { scaleIn() },
                            popEnterTransition = { scaleIn() },
                            exitTransition = { fadeOut() },
                            popExitTransition = { fadeOut() }
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color
                            )
                        }

                        composable(
                            route = Screen.AuthenticationScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                            arguments = listOf(
                                navArgument(
                                    name = "noteId"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                },
                                navArgument(
                                    name = "noteColor"
                                ) {
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            ),
                            enterTransition = {
                                slideInVertically(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            },
                            popEnterTransition = {
                                slideInVertically(
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            },
                            exitTransition = { fadeOut() },
                            popExitTransition = { fadeOut() }
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            val id = it.arguments?.getInt("noteId") ?: -1

                            AuthenticationScreen(
                                navController = navController,
                                noteColor = color,
                                noteId = id
                            )
                        }

                    }
                }
            }
        }
    }
}

