package com.kotlin.easyrent.features.rentalManagement.domain.repository

import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.utils.ServiceResponse
import kotlinx.coroutines.flow.Flow

interface RentalsRepository {
    //add, update, delete, get
    suspend fun upsertRental(rental: Rental) : ServiceResponse<Unit>
    suspend fun deleteRental(rental: Rental) : ServiceResponse<Unit>
    suspend fun getRentalById(id: String) : ServiceResponse<Rental?>
    fun getAllRentals() : Flow<ServiceResponse<List<Rental>>>
    suspend fun getAllDeletedRentals() : ServiceResponse<List<Rental>>
    suspend fun getAllUnsyncedRentals() : ServiceResponse<List<Rental>>
}