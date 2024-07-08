package com.kotlin.easyrent.features.rentalManagement.data.mapper

import com.kotlin.easyrent.features.rentalManagement.data.modal.RentalEntity
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental

fun RentalEntity.toDomain() : Rental {
    return Rental(
        id = id,
        name = name,
        location,
        monthlyPayment = monthlyPayment,
        noOfRooms = noOfRooms,
        description = description,
        image = image,
        isSynced = isSynced,
        isDeleted = isDeleted
    )
}

fun Rental.toEntity() : RentalEntity {
    return RentalEntity(
        id = id,
        name = name,
        location = location,
        monthlyPayment = monthlyPayment,
        noOfRooms = noOfRooms,
        description = description,
        image = image,
        isSynced = isSynced,
        isDeleted = isDeleted
    )
}