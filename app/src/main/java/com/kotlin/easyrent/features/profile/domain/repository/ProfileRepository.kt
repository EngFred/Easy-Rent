package com.kotlin.easyrent.features.profile.domain.repository

import com.kotlin.easyrent.features.auth.domain.modal.User
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.flow.Flow
interface ProfileRepository {
    suspend fun updateProfilePhoto(photoUrl: String) : ServiceResponse<Any>
    suspend fun updateBio(bio: String) : ServiceResponse<Any>
    suspend fun updateUsername(lastName: String, firstName: String) : ServiceResponse<Any>
    suspend fun updateTel(tel: String) : ServiceResponse<Any>
    suspend fun updateAddress(address: String) : ServiceResponse<Any>
    suspend fun updateDOB(dob: Long) : ServiceResponse<Any>
    suspend fun logout() : ServiceResponse<Unit>
    fun getLandlord() : Flow<ServiceResponse<User?>>
}