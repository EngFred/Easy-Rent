package com.kotlin.easyrent.features.tenantManagement.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.rentalManagement.domain.modal.Rental
import com.kotlin.easyrent.features.rentalManagement.domain.usecase.GetAllRentalsUseCase
import com.kotlin.easyrent.features.tenantManagement.domain.usecase.GetAllTenantsUseCase
import com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants.TenantsUiEvents
import com.kotlin.easyrent.features.tenantManagement.ui.screens.tenants.TenantsUiState
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TenantsViewModel @Inject constructor(
    private val getAllTenantsUseCase: GetAllTenantsUseCase,
    private val getAllRentalsUseCase: GetAllRentalsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TenantsUiState())
    val uiState = _uiState.asStateFlow()


    init {
        getAllTenants()
        getAllRentals()
    }

    fun onEvent(event: TenantsUiEvents){
        when(event){
            TenantsUiEvents.SelectedAllTenants -> {
                if ( _uiState.value.selectedRental != null ) {
                    _uiState.update {
                        it.copy(
                            selectedRental = null,
                            filteredTenants = it.allTenants
                        )
                    }
                }
            }
            is TenantsUiEvents.SelectedRental -> {
                if ( _uiState.value.selectedRental != event.rental ) {
                    _uiState.update {
                        it.copy(
                            selectedRental = event.rental
                        )
                    }
                }
                filterTenantsByRental(_uiState.value.selectedRental!!)
            }
        }
    }

    private fun filterTenantsByRental(selectedRental: Rental) {
        val filteredTenants = _uiState.value.allTenants.filter { tenant ->
            tenant.rentalId == selectedRental.id
        }

        _uiState.update {
            it.copy(
                filteredTenants = filteredTenants
            )
        }
    }

    private fun getAllTenants() = viewModelScope.launch {
        getAllTenantsUseCase().collect { res ->
            when(res) {
                is ServiceResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = res.message
                        )
                    }
                }
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            allTenants = res.data
                        )
                    }
                }
            }
        }
    }

    private fun getAllRentals() = viewModelScope.launch {
        getAllRentalsUseCase().collect { res ->
            when(res) {
                is ServiceResponse.Error -> Unit
                ServiceResponse.Idle -> Unit
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            rentals = res.data
                        )
                    }
                }
            }
        }
    }

}