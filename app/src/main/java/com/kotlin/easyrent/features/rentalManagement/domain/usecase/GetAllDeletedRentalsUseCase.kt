package com.kotlin.easyrent.features.rentalManagement.domain.usecase

import com.kotlin.easyrent.features.rentalManagement.domain.repository.RentalsRepository
import javax.inject.Inject

class GetAllDeletedRentalsUseCase @Inject constructor(
    private val rentalRepository: RentalsRepository
) {
    suspend operator fun invoke() = rentalRepository.getAllDeletedRentals()
}