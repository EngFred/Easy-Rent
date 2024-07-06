package com.kotlin.easyrent.features.auth.domain.usecase

import com.kotlin.easyrent.R

class ValidateConfirmPasswordUseCase {
    /**
     * Executes the password validation.
     * @param password The password to validate.
     * @param confirmPassword The password to confirm.
     * @return True if the password is at least 8 characters long, false otherwise.
     */
    fun execute(password: String, confirmPassword: String): Int? {
        return when{
            password != confirmPassword -> R.string.password_dont_match
            else -> null
        }
    }
}
