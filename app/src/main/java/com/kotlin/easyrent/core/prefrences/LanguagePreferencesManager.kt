package com.kotlin.easyrent.core.prefrences

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

enum class Language {
    English,
    Luganda,
    Swahili
}

class LanguagePreferencesManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
){

    companion object {
        val LANGUAGE = stringPreferencesKey("language")
        private val TAG = Preferences::class.java.simpleName
    }

    suspend fun saveLanguage(language: Language) {
        try {
            dataStore.edit { preferences ->
                preferences[LANGUAGE] = language.name.lowercase()
            }
        } catch (e: Exception) {
            Log.d(TAG, "${e.message}")
        }
    }

    val getLanguage : Flow<String> = dataStore.data.map { pref ->
        pref[LANGUAGE] ?: Language.English.name.lowercase()
    }.distinctUntilChanged().catch { throwable ->
        Log.d(TAG, throwable.message.toString())
    }.flowOn(Dispatchers.IO)

}