package org.example.mandm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.repo.CustomerRepository
import org.example.mandm.repo.RouteRepository

data class DashboardUiState(
    val customers: List<CustomerEntity> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

class DashboardViewModel(
    private val customerRepo: CustomerRepository,
    private val routeRepo: RouteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadCustomersForRoute(routeId: Int) {
        _uiState.value = _uiState.value.copy(isLoading = true, message = null)
        viewModelScope.launch {
            routeRepo.getCustomersForRoute(routeId)
                .catch { t ->
                    _uiState.value = _uiState.value.copy(isLoading = false, message = t.message ?: "Failed to load customers")
                }
                .collectLatest { customers ->
                    _uiState.value = _uiState.value.copy(customers = customers, isLoading = false, message = null)
                }
        }
    }

    fun createCustomer(customer: CustomerEntity, routeId: Int? = null) {
        _uiState.value = _uiState.value.copy(isLoading = true, message = null)
        viewModelScope.launch {
            try {
                val newId = customerRepo.insertCustomer(customer)
                customer.userId=newId
//                if (routeId != null) {
//                    routeRepo.addCustomerToRoute(newId, routeId)
//                }
                // if routeId provided, stream will emit via loadCustomersForRoute collection
                _uiState.value = _uiState.value.copy(isLoading = false, message = null)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(isLoading = false, message = t.message ?: "Failed to create customer")
            }
        }
    }
}


