package com.kotlin.easyrent.core.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.core.prefrences.Language
import com.kotlin.easyrent.core.prefrences.LanguagePreferencesManager
import com.kotlin.easyrent.core.usecses.GetLandlordUseCase
import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.utils.ServiceResponse
import com.kotlin.easyrent.utils.restartApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val languagePreferencesManager: LanguagePreferencesManager,
    private val getLandlordUseCase: GetLandlordUseCase
) : ViewModel() {

    private val _currentLanguage = MutableStateFlow<String?>(null)
    val currentLanguage = _currentLanguage.asStateFlow()

    private val _loggedInUser = MutableStateFlow<User?>(null)
    val loggedInUser = _loggedInUser.asStateFlow()

    init {
        getLanguage()
        getLandlord()
    }

    private fun getLanguage() = viewModelScope.launch {
        languagePreferencesManager.getLanguage.collect {
            _currentLanguage.value = it
        }
    }

    private fun getLandlord() = viewModelScope.launch {
        getLandlordUseCase.invoke().collectLatest { res ->
            when(res) {
                is ServiceResponse.Error -> Unit
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _loggedInUser.value = res.data
                }
            }
        }
    }

    fun setLanguage(language: String, context: Context) = viewModelScope.launch(Dispatchers.IO) {

        val selectedLanguage = Language.valueOf(language)
        languagePreferencesManager.saveLanguage(selectedLanguage)
        restartApplication(context)
    }

    fun setLoggedInUser(user: User?) {
        _loggedInUser.value = user
    }

}