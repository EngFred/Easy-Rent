package com.kotlin.easyrent.features.auth.domain.usecase

import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.features.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(user: User, password: String) = authRepository.signup(user, password)
}