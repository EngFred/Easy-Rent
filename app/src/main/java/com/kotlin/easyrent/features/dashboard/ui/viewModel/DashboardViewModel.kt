package com.kotlin.easyrent.features.dashboard.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.dashboard.domain.usecase.GetExpectedRevenueUseCase
import com.kotlin.easyrent.features.dashboard.domain.usecase.GetRentalsCountUseCase
import com.kotlin.easyrent.features.dashboard.domain.usecase.GetTenantsCountUseCase
import com.kotlin.easyrent.features.dashboard.domain.usecase.GetTotalExpensesUseCase
import com.kotlin.easyrent.features.dashboard.domain.usecase.GetTotalPaymentsUseCase
import com.kotlin.easyrent.features.dashboard.ui.screens.DashboardUiState
import com.kotlin.easyrent.utils.ServiceResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getTotalExpensesUseCase: GetTotalExpensesUseCase,
    private val getTotalPaymentsUseCase: GetTotalPaymentsUseCase,
    private val getExpectedRevenueUseCase: GetExpectedRevenueUseCase,
    private val getTenantsCountUseCase: GetTenantsCountUseCase,
    private val getRentalsCountUseCase: GetRentalsCountUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTotalPayments()
        getTotalExpenses()
        getExpectedRevenue()
        getTenantsCount()
        getRentalsCount()
    }

    private fun getTotalPayments() = viewModelScope.launch {
        getTotalPaymentsUseCase.invoke().collectLatest { result ->
            when(result) {
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            totalPayments = result.data
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    private fun getTotalExpenses() = viewModelScope.launch {
        getTotalExpensesUseCase.invoke().collectLatest { result ->
            when(result) {
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            totalExpenses = result.data
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    private fun getExpectedRevenue() = viewModelScope.launch {
        getExpectedRevenueUseCase.invoke().collectLatest { result ->
            when(result) {
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            expectedRevenue = result.data
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    private fun getTenantsCount() = viewModelScope.launch {
        getTenantsCountUseCase.invoke().collectLatest { result ->
            when(result){
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            totalTenants = result.data
                        )
                    }
                }
                else -> Unit
            }

        }
    }

    private fun getRentalsCount() = viewModelScope.launch {
        getRentalsCountUseCase.invoke().collectLatest { result ->
            when(result){
                is ServiceResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            totalRentals = result.data
                        )
                    }
                }
                else -> Unit
            }

        }
    }
}