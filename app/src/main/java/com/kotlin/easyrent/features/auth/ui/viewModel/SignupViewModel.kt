package com.kotlin.easyrent.features.auth.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.features.auth.domain.usecase.SignupUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateConfirmPasswordUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateEmailUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidateNameUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidatePasswordUseCase
import com.kotlin.easyrent.features.auth.domain.usecase.ValidatePhoneNumberUseCase
import com.kotlin.easyrent.features.auth.ui.screens.signup.SignupUiEvents
import com.kotlin.easyrent.features.auth.ui.screens.signup.SignupUiState
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateConfirmPasswordUseCase: ValidateConfirmPasswordUseCase,
    private val validateNameUseCase: ValidateNameUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent( event: SignupUiEvents) {
        if ( _uiState.value.signupError != null ) {
            _uiState.update {
                it.copy(
                    signupError = null
                )
            }
        }
        when(event) {
            is SignupUiEvents.AddressChanged -> {
                _uiState.update {
                    it.copy(
                        address = event.address
                    )
                }
            }
            is SignupUiEvents.ConfirmPasswordChanged -> {
                val validationResult = validateConfirmPasswordUseCase.execute(_uiState.value.password, event.confirmPassword)
                _uiState.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        confirmPasswordError = validationResult
                    )
                }
            }
            is SignupUiEvents.ContactNumberChanged -> {
                val validationResult = validatePhoneNumberUseCase.execute( event.contactNumber)
                _uiState.update {
                    it.copy(
                        contactNumber = event.contactNumber,
                        contactNumberError = validationResult
                    )
                }
            }
            is SignupUiEvents.DOBChanged -> {
                _uiState.update {
                    it.copy(
                        dob = event.dob
                    )
                }
            }
            is SignupUiEvents.EmailChanged -> {
                val validationResult = validateEmailUseCase.execute( event.email)
                _uiState.update {
                    it.copy(
                        email = event.email,
                        emailError = validationResult
                    )
                }
            }
            is SignupUiEvents.FirstNameChanged -> {
                val validationResult = validateNameUseCase.execute( event.firstName)
                _uiState.update {
                    it.copy(
                        firstName = event.firstName,
                        firstNameError = validationResult
                    )
                }
            }
            is SignupUiEvents.LastNameChanged -> {
                val validationResult = validateNameUseCase.execute( event.lastName)
                _uiState.update {
                    it.copy(
                        lastName = event.lastName,
                        lastNameError = validationResult
                    )
                }
            }
            is SignupUiEvents.PasswordChanged -> {
                val validationResult = validatePasswordUseCase.execute(event.password)
                _uiState.update {
                    it.copy(
                        password = event.password,
                        passwordError = validationResult
                    )
                }
            }
            SignupUiEvents.ConfirmButtonClicked -> {
                if (_uiState.value.isValidForm) {
                    _uiState.update {
                        it.copy(
                            signupInProgress = true
                        )
                    }
                    registerUser()
                }
            }
        }
        validateForm()
    }

    private fun validateForm() {
        _uiState.update {
            it.copy(
                isValidForm = _uiState.value.passwordError == null && _uiState.value.confirmPasswordError == null && _uiState.value.emailError == null && _uiState.value.firstNameError == null && _uiState.value.lastNameError == null && _uiState.value.contactNumberError == null && _uiState.value.confirmPassword.isNotEmpty()
            )
        }
    }

    private fun registerUser() = viewModelScope.launch( Dispatchers.IO ) {
        val user = User(
            id = UUID.randomUUID().toString(),
            firstName = _uiState.value.firstName.trim(),
            lastName = _uiState.value.lastName.trim(),
            email = _uiState.value.email.trim(),
            contactNumber = _uiState.value.contactNumber.trim(),
            dateOfBirth = _uiState.value.dob?.trim(),
            address = _uiState.value.address?.trim()
        )
        val response = signupUseCase.invoke(user, _uiState.value.password)
        Log.d("SignupViewModel", "$user")
        when(response){
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        signupError = response.message,
                        signupInProgress = false
                    )
                }
            }
            ServiceResponse.Loading -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        signupSuccess = true
                    )
                }
            }
        }
    }
}