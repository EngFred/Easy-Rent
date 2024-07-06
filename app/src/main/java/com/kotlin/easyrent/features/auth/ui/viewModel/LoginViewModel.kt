package com.kotlin.easyrent.features.auth.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.auth.domain.usecase.LoginUseCase
import com.kotlin.easyrent.features.auth.ui.screens.login.LoginUiEvents
import com.kotlin.easyrent.features.auth.ui.screens.login.LoginUiState
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvents) {

        if ( _uiState.value.loginError != null ) {
            _uiState.update {
                it.copy(
                    loginError = null
                )
            }
        }
        when(event) {
            LoginUiEvents.ConfirmButtonClicked -> {
                if (_uiState.value.isFormValid) {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    login(_uiState.value.email, _uiState.value.password)
                }
            }
            is LoginUiEvents.EmailChanged -> {
                _uiState.update {
                    it.copy(
                        email = event.email
                    )
                }
            }
            is LoginUiEvents.PasswordChanged -> {
                _uiState.update {
                    it.copy(
                        password = event.password
                    )
                }
            }
        }

        validateForm()
    }

    private fun validateForm() {
        _uiState.update {
            it.copy(
                isFormValid = it.email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(it.email).matches() && it.password.isNotBlank() && it.loginError == null
            )
        }
    }

    private fun login(email: String, password: String) = viewModelScope.launch( Dispatchers.IO ) {
        val response = loginUseCase.invoke(email.trim(), password.trim())
        Log.i("LoginViewModel", "$email : $password")

        when(response) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginError = response.message
                    )
                }
            }
            ServiceResponse.Loading -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        loginSuccess = true
                    )
                }
            }
        }
    }
}