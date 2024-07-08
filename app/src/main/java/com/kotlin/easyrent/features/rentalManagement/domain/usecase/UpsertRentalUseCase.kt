package com.kotlin.easyrent.features.rentalManagement.domain.usecase

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.rentalManagement.domain.repository.RentalsRepository
import javax.inject.Inject

class UpsertRentalUseCase @Inject constructor(
    private val rentalRepository: RentalsRepository
) {
    suspend operator fun invoke(rental: Rental) = rentalRepository.upsertRental(rental)
}