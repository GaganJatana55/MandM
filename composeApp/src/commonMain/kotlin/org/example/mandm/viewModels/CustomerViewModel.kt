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

data class CustomerUiState(
    val customers: List<CustomerEntity> = emptyList(),
    val isLoading: Boolean = false,
    val message: String? = null
)

class CustomerViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()

    fun observeAllCustomers() {
        _uiState.value = _uiState.value.copy(isLoading = true, message = null)
        viewModelScope.launch {
            customerRepository.getAllCustomers()
                .catch { t ->
                    _uiState.value = _uiState.value.copy(isLoading = false, message = t.message ?: "Failed to load customers")
                }
                .collectLatest { list ->
                    _uiState.value = _uiState.value.copy(customers = list, isLoading = false, message = null)
                }
        }
    }
}


