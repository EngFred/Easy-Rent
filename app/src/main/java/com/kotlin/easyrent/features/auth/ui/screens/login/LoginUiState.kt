package com.kotlin.easyrent.features.auth.ui.screens.login

import androidx.annotation.StringRes
import com.kotlin.easyrent.features.auth.domain.modal.User

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    @StringRes
    val loginError: Int? = null,

    val loginSuccess: Boolean = false,
    val isFormValid: Boolean = false,
    val loggedInUser: User? = null
)