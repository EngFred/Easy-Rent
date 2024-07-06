package com.kotlin.easyrent.features.auth.domain.repository

import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.utils.ServiceResponse

interface AuthRepository {
    suspend fun login(username: String, password: String): ServiceResponse<Boolean>
    suspend fun signup(user: User, password: String): ServiceResponse<Boolean>
}