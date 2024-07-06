package com.kotlin.easyrent.features.auth.domain.usecase

import androidx.annotation.StringRes
import com.kotlin.easyrent.R

class ValidatePasswordUseCase {
    /**
     * Executes the password validation.
     * @param password The password to validate.
     * @return True if the password is at least 8 characters long, false otherwise.
     */
    fun execute(password: String): Int? {
        return when {
            password.length < 8 -> R.string.short_password
            password.isEmpty() -> R.string.empty_password
            else -> null
        }
    }
}
