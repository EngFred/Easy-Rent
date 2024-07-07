package com.kotlin.easyrent.features.profile.domain.usecase

import com.kotlin.easyrent.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateAddressUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    suspend operator fun invoke(address: String) = profileRepository.updateAddress(address)
}