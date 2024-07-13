package com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert

sealed class UpsertRentalUiEvents {
    data class NameChanged(val name: String) : UpsertRentalUiEvents()
    data class LocationChanged(val location: String) : UpsertRentalUiEvents()
    data class MonthlyPaymentChanged(val payment: String) : UpsertRentalUiEvents()
    data class NumberOfRoomsChanged(val numberOfRooms: String) : UpsertRentalUiEvents()
    data class ImageUrlChanged(val imageUrl: String) : UpsertRentalUiEvents()
    data class DescriptionChanged(val description: String) : UpsertRentalUiEvents()
    data object AddedRental : UpsertRentalUiEvents()
    data object DeletedRental : UpsertRentalUiEvents()
    data object ShowPhotoOptionsDialogToggled : UpsertRentalUiEvents()
    data object ShowConfirmDeleteDialogToggled : UpsertRentalUiEvents()

}