package com.kotlin.easyrent.features.profile.domain.usecase

import com.kotlin.easyrent.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateUsernameUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(lastName: String, firstName: String) = profileRepository.updateUsername(lastName, firstName)
}