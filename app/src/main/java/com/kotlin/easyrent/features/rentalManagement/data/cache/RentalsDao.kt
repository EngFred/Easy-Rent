package com.kotlin.easyrent.features.rentalManagement.data.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.kotlin.easyrent.features.rentalManagement.data.modal.RentalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RentalsDao {
    @Upsert
    suspend fun upsertRental(rental: RentalEntity)

    @Query("SELECT * FROM rentals WHERE id = :rentalId")
    fun getRentalById(rentalId: String): Flow<RentalEntity?>

    @Query("SELECT * FROM rentals WHERE isDeleted = 0 ORDER BY id ASC")
    fun getAllRentals(): Flow<List<RentalEntity>>

    @Query("SELECT * FROM rentals WHERE isDeleted = 1 AND isSynced = 0")
    fun getAllDeletedRentals(): Flow<List<RentalEntity>>

    @Query("DELETE FROM rentals WHERE id = :rentalId")
    suspend fun deleteRental(rentalId: String)

    @Query("SELECT * FROM rentals WHERE isSynced = 0 AND isDeleted = 0")
    fun getAllUnsyncedRentals(): Flow<List<RentalEntity>>
}