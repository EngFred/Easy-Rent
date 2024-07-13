package com.kotlin.easyrent.features.rentalManagement.domain.modal

data class Rental(
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
) {
    constructor() : this(
        "",
        "",
        "",
        0.0,
        0,
        0
    )
}