package com.kotlin.easyrent.features.rentalManagement.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.DeleteRentalUseCase
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetAllDeletedRentalsUseCase
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetAllRentalsUseCase
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetAllUnsyncedRentalsUseCase
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.UpsertRentalUseCase
import com.kotlin.easyrent.features.rentalManagement.ui.screens.rentals.RentalsUiEvents
import com.kotlin.easyrent.features.rentalManagement.ui.screens.rentals.RentalsUiState
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentalsViewModel @Inject constructor(
    private val upsertRentalUseCase: UpsertRentalUseCase,
    private val getAllRentalsUseCase: GetAllRentalsUseCase,
    private val getAllDeletedRentalsUseCase: GetAllDeletedRentalsUseCase,
    private val getAllUnsyncedRentalsUseCase: GetAllUnsyncedRentalsUseCase,
    private val deleteRentalUseCase: DeleteRentalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RentalsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllRentals()
    }


    fun onEvent(event: RentalsUiEvents) {
        when(event) {
            is RentalsUiEvents.RentalDeleted -> {
                _uiState.update {
                    it.copy(
                        deletedRentalId = event.rental.id,
                        deletingRental = true
                    )
                }
                deleteRental(event.rental)
            }
        }
    }

    private fun deleteRental(rental: Rental) = viewModelScope.launch( Dispatchers.IO ) {
        val res = deleteRentalUseCase.invoke(rental)
        Log.v("TAG", "Task done!")
        when(res) {
            is ServiceResponse.Error -> {
                _uiState.update {
                    it.copy(
                        deletedRentalId = "",
                        deletingRental = false,
                        rentalDeleteError = res.message
                    )
                }
            }
            ServiceResponse.Idle -> Unit
            is ServiceResponse.Success -> {
                _uiState.update {
                    it.copy(
                        deletedRentalId = "",
                        deletingRental = false
                    )
                }
            }
        }
    }

    private fun getAllRentals() = viewModelScope.launch {
        getAllRentalsUseCase.invoke().collectLatest { res ->
            when(res) {
                is ServiceResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loadError = res.message
                        )
                    }
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            rentals = res.data
                        )
                    }
                }
            }
        }
    }

}