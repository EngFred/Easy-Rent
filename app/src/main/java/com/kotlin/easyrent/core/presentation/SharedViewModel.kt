package com.kotlin.easyrent.core.presentation

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.prefrences.LanguagePreferencesManager
import com.kotlin.easyrent.utils.restartApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val languagePreferencesManager: LanguagePreferencesManager
) : ViewModel() {


    private val _currentLanguage = MutableStateFlow<String?>(null)
    val currentLanguage = _currentLanguage.asStateFlow()

    init {
        getLanguage()
    }

    private fun getLanguage() = viewModelScope.launch {
        languagePreferencesManager.getLanguage.collect {
            _currentLanguage.value = it
        }
    }

    fun setLanguage(language: String, context: Context) = viewModelScope.launch(Dispatchers.IO) {

        val selectedLanguage = Language.valueOf(language)
        languagePreferencesManager.saveLanguage(selectedLanguage)
        restartApplication(context)
    }
}