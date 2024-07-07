package com.kotlin.easyrent.features.profile.domain.usecase

import com.kotlin.easyrent.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateTelUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(tel: String) = profileRepository.updateTel(tel)
}