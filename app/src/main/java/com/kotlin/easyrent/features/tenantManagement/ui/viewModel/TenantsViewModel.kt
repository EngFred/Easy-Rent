package com.kotlin.easyrent.features.tenantManagement.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.easyrent.features.tenantManagement.domain.usecase.GetAllTenantsUseCase
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
    private val getAllTenantsUseCase: GetAllTenantsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TenantsUiState())
    val uiState = _uiState.asStateFlow()


    init {
        getAllTenants()
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
                            tenants = res.data
                        )
                    }
                }
            }
        }
    }

}