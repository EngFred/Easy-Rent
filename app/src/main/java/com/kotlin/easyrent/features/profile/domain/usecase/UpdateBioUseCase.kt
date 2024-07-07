package com.kotlin.easyrent.features.profile.domain.usecase

import com.kotlin.easyrent.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateBioUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(bio: String) = profileRepository.updateBio(bio)
}