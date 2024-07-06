package com.kotlin.easyrent.features.auth.domain.usecase

import com.kotlin.easyrent.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) = authRepository.login(email, password)
}