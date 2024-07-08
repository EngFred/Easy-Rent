package com.kotlin.easyrent.features.rentalManagement.domain.usecase

import com.kotlin.easyrent.features.rentalManagement.domain.repository.RentalsRepository
import javax.inject.Inject

class GetAllRentalsUseCase @Inject constructor(
    private val rentalRepository: RentalsRepository
) {
    operator fun invoke() = rentalRepository.getAllRentals()
}