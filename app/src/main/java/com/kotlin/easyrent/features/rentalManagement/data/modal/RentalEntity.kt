package com.kotlin.easyrent.features.rentalManagement.data.modal

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rentals")
data class RentalEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val location: String,
    val monthlyPayment: Double,
    val noOfRooms: Int,
    val occupiedRooms: Int,
    val description: String? = null,
    val image: String? = null,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
)
