package com.kotlin.easyrent.features.auth.ui.screens.login

sealed class LoginUiEvents {
    data class EmailChanged( val email: String ) : LoginUiEvents()
    data class PasswordChanged( val password: String ) : LoginUiEvents()
    data object ConfirmButtonClicked: LoginUiEvents()
}