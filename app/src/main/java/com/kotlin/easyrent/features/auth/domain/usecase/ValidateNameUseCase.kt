package com.kotlin.easyrent.features.auth.domain.usecase

import com.kotlin.easyrent.R

/**
 * Use case for validating first names.
 */
class ValidateNameUseCase {
    /**
     * Executes the first name validation.
     * @param name The name to validate.
     * @return True if the first name is at least 3 characters long, false otherwise.
     */
    fun execute(name: String): Int? {
        return when{
            name.length < 3 -> R.string.short_first_name
            name.isEmpty() -> R.string.empty_first_name
            else -> null
        }
    }
}
