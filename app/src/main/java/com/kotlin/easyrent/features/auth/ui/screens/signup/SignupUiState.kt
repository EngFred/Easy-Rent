package com.kotlin.easyrent.features.auth.ui.screens.signup

import androidx.annotation.StringRes

data class SignupUiState(
    val lastName: String = "",
    @StringRes
    val lastNameError: Int? = null,
    val firstName: String = "",
    @StringRes
    val firstNameError: Int? = null,
    val email: String = "",
    @StringRes
    val emailError: Int? = null,
    val dob: Long? = null,
    val contactNumber: String = "",
    @StringRes
    val contactNumberError: Int? = null,
    val address: String? = null,
    val password: String = "",
    @StringRes
    val passwordError: Int? = null,
    val confirmPassword: String = "",
    @StringRes
    val confirmPasswordError: Int? = null,

    @StringRes
    val signupError: Int? = null,
    val signupInProgress: Boolean = false,

    val isValidForm: Boolean = false,
    val signupSuccess: Boolean = false
)
