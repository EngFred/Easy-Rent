package com.kotlin.easyrent.features.auth.ui.screens.signup

sealed class SignupUiEvents {
    data class LastNameChanged( val lastName: String ) : SignupUiEvents()
    data class FirstNameChanged( val firstName: String ) : SignupUiEvents()
    data class EmailChanged( val email: String ) : SignupUiEvents()
    data class DOBChanged( val dob: Long ) : SignupUiEvents()
    data class ContactNumberChanged( val contactNumber: String ) : SignupUiEvents()
    data class AddressChanged( val address: String ) : SignupUiEvents()
    data class PasswordChanged( val password: String ) : SignupUiEvents()
    data class ConfirmPasswordChanged( val confirmPassword: String ) : SignupUiEvents()
    data object ConfirmButtonClicked: SignupUiEvents()
}