package com.kotlin.easyrent.core.usecses

import com.kotlin.easyrent.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject

class GetLandlordUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {
    operator fun invoke() = profileRepository.getLandlord()
}