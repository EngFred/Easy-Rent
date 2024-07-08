package com.kotlin.easyrent.features.rentalManagement.ui.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.R
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetRentalByIdUseCase
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.UpsertRentalUseCase
import com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert.UpsertRentalUiEvents
import com.kotlin.easyrent.features.rentalManagement.ui.screens.upsert.UpsertRentalUiState
import com.kotlin.easyrent.utils.Keys
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UpsertRentalViewModel @Inject constructor(
    private val upsertRentalUseCase: UpsertRentalUseCase,
    private val getRentalByIdUseCase: GetRentalByIdUseCase,
    savedStateHandle: SavedStateHandle
) :  ViewModel() {

    private val rentalId = savedStateHandle.get<String>(Keys.RENTAL_ID)

    private val _uiState = MutableStateFlow(UpsertRentalUiState())
    val uiState = _uiState.asStateFlow()


    init {

        if ( rentalId != null ) {
            getRentalById(rentalId)
        } else {
            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }

    }


    fun onEvent( event: UpsertRentalUiEvents ) {
        if( _uiState.value.upsertError !=  null || _uiState.value.fetchError != null) {
            _uiState.update {
                it.copy(
                    upsertError = null,
                    fetchError = null
                )
            }
        }
        when(event) {
            is UpsertRentalUiEvents.DescriptionChanged -> {
                _uiState.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is UpsertRentalUiEvents.ImageUrlChanged -> {
                _uiState.update {
                    it.copy(
                        imageUrl = event.imageUrl
                    )
                }
            }
            is UpsertRentalUiEvents.LocationChanged -> {
                _uiState.update {
                    it.copy(
                        location = event.location
                    )
                }
                validateLocation(_uiState.value.location ?: "")
            }
            is UpsertRentalUiEvents.MonthlyPaymentChanged -> {
                _uiState.update {
                    it.copy(
                        monthlyPayment = event.payment
                    )
                }
                validateMonthlyPayment(_uiState.value.monthlyPayment ?: "")
            }
            is UpsertRentalUiEvents.NameChanged -> {
                _uiState.update {
                    it.copy(
                        name = event.name
                    )
                }
                validateName(_uiState.value.name ?: "")
            }
            is UpsertRentalUiEvents.NumberOfRoomsChanged -> {
                _uiState.update {
                    it.copy(
                        noOfRooms = event.numberOfRooms
                    )
                }
                validateNumberOfRooms( _uiState.value.noOfRooms ?: "" )
            }
            UpsertRentalUiEvents.AddedRental -> {
                if ( _uiState.value.isFormValid ) {
                    addUpdateRental()
                }
            }

            UpsertRentalUiEvents.ShowPhotoOptionsDialogToggled -> {
                _uiState.update {
                    it.copy(
                        showPhotoOptionsDialog = !it.showPhotoOptionsDialog
                    )
                }
            }
        }
        validateForm()
    }

    private fun validateForm(){
        val nameValidationResults = !_uiState.value.name.isNullOrEmpty() && _uiState.value.name!!.length >= 3
        val locationValidationResults = !_uiState.value.location.isNullOrEmpty()
        val noOfRoomsValidationResults = !_uiState.value.noOfRooms.isNullOrEmpty()  && _uiState.value.noOfRooms != "0" && _uiState.value.noOfRooms?.toIntOrNull() != null
        val monthlyPaymentValidationResults = !_uiState.value.monthlyPayment.isNullOrEmpty()  && _uiState.value.monthlyPayment != "0" && _uiState.value.monthlyPayment?.toIntOrNull() != null

        val validationResults = nameValidationResults && locationValidationResults && noOfRoomsValidationResults && monthlyPaymentValidationResults
        Log.v("#", "Validation results $validationResults\nNameResults: $nameValidationResults\nLocationResults: $locationValidationResults\nNoOfRoomsResults: $noOfRoomsValidationResults\nMonthlyPaymentResults: $monthlyPaymentValidationResults")
        _uiState.update {
            it.copy(
                isFormValid = validationResults
            )
        }
    }

    private fun addUpdateRental() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                upserting = true
            )
        }

        val rental = Rental(
            id = _uiState.value.rentalId ?: UUID.randomUUID().toString(),
            name = _uiState.value.name!!,
            location = _uiState.value.location!!,
            image = _uiState.value.imageUrl,
            noOfRooms = _uiState.value.noOfRooms!!.toInt(),
            monthlyPayment = _uiState.value.monthlyPayment!!.toLong(),
            description = _uiState.value.description
        )
        val res = upsertRentalUseCase.invoke(rental)
        Log.v("TAG", "Task done!")
        when(res) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        upserting = false,
                        upsertError = res.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        upsertSuccessful = true
                    )
                }
            }
        }

    }

    private fun getRentalById(rentalId: String) = viewModelScope.launch {
        val res = getRentalByIdUseCase.invoke(rentalId)
        Log.v("TAG", "Task done!")
        when(res) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        fetchError = res.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = res.data?.name?.trim(),
                        location = res.data?.location?.trim(),
                        noOfRooms = res.data?.noOfRooms?.toString(),
                        imageUrl = res.data?.image?.trim(),
                        monthlyPayment = res.data?.monthlyPayment?.toString(),
                        rentalId = res.data?.id,
                        description = res.data?.description?.trim()
                    )
                }
            }
        }
    }


    private fun  validateName( name: String ) {
        val nameError = when {
            name.isEmpty() -> R.string.empty_name
            name.length < 3 -> R.string.short_name
            else -> null
        }
        _uiState.update {
            it.copy(
                nameError =  nameError
            )
        }
    }

    private fun  validateLocation( location: String ) {
        val locationError = when {
            location.isEmpty() -> R.string.empty_location
            else -> null
        }
        _uiState.update {
            it.copy(
                locationError = locationError
            )
        }
    }

    private fun  validateMonthlyPayment(monthlyPayment: String) {
        val monthlyPaymentError = when {
            monthlyPayment.isEmpty() || monthlyPayment == "0" -> R.string.empty_payment
            monthlyPayment.toIntOrNull() == null -> R.string.invalid_payment
            else -> null
        }
        _uiState.update {
            it.copy(
                monthlyPaymentError = monthlyPaymentError
            )
        }
    }

    private fun  validateNumberOfRooms(noOfRooms: String) {
        val noOfRoomsError = when {
            noOfRooms.isEmpty() || noOfRooms == "0" -> R.string.empty_rooms
            noOfRooms.toIntOrNull() == null -> R.string.invalid_number_of_rooms
            else -> null
        }
        _uiState.update {
            it.copy(
                noOfRoomsError = noOfRoomsError
            )
        }
    }
}