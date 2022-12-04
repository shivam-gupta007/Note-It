package com.app.noteit.feature_note.data.data_source.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PasscodeDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userPin")
        val USER_PIN_KEY = stringPreferencesKey(name = "userPin")
    }

    val getPin: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USER_PIN_KEY] ?: ""
        }

    suspend fun setPin(pin: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_PIN_KEY] = pin
        }
    }

}