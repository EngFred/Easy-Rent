package com.kotlin.easyrent.features.rentalManagement.domain.modal

data class Rental(
    val id: String,
    val name: String,
    val location: String,
    val monthlyPayment: Long,
    val noOfRooms: Int,
    val description: String? = null,
    val image: String? = null,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false
) {
    constructor() : this(
        "","","",0L,0
    )
}