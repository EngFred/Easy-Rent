package com.kotlin.easyrent.features.auth.domain.usecase

import com.kotlin.easyrent.R

class ValidatePhoneNumberUseCase {
    fun execute(phoneNumber: String): Int? {
        return when{
            !phoneNumber.isValidPhoneNumber() -> R.string.invalid_phone_number
            phoneNumber.isEmpty() -> R.string.empty_phone_number
            else -> null
        }
    }
}
fun String.isValidPhoneNumber(): Boolean {
    val phoneNumberPattern = "^\\+?[0-9]{10,15}$".toRegex()
    return phoneNumberPattern.matches(this)
}