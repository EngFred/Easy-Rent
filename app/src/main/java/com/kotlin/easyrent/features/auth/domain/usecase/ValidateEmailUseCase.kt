package com.kotlin.easyrent.features.auth.domain.usecase

import com.kotlin.easyrent.R

class ValidateEmailUseCase {
    fun execute(email: String): Int? {
        return when{
            email.isEmpty() -> R.string.empty_email
            !email.isValidEmail() -> R.string.invalid_email
            else -> null
        }
    }
}
fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}