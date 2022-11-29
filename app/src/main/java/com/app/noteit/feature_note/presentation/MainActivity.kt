package com.app.noteit.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.app.noteit.feature_note.presentation.add_edit_notes.components.AddEditNoteScreen
import com.app.noteit.feature_note.presentation.notes.components.NotesScreen
import com.app.noteit.feature_note.presentation.util.Screen
import com.app.noteit.ui.theme.NoteItTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteItTheme {

                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberAnimatedNavController()

                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Screen.NotesScreen.route,
                    ) {
                        composable(
                            route = Screen.NotesScreen.route,
                            enterTransition = {
                                fadeIn()
                            },
                            popEnterTransition = {
                                fadeIn()
                            },
                            exitTransition = {
                                fadeOut()
                            },
                            popExitTransition = {
                                fadeOut()
                            }
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
                            enterTransition = {
                                scaleIn()
                            },
                            popEnterTransition = {
                                scaleIn()
                            },
                            exitTransition = {
                                fadeOut()
                            },
                            popExitTransition = {
                                fadeOut()
                            }
                        ) {
                            val color = it.arguments?.getInt("noteColor") ?: -1
                            AddEditNoteScreen(
                                navController = navController,
                                noteColor = color
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun DefaultPreview() {
    NoteItTheme {

    }
}