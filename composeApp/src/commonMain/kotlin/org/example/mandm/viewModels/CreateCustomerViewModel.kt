package org.example.mandm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.mandm.DateTimeUtil
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.repo.CustomerRepository

data class CreateCustomerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val editingCustomer: CustomerEntity? = null
)

class CreateCustomerViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateCustomerUiState())
    val uiState: StateFlow<CreateCustomerUiState> = _uiState.asStateFlow()

    private val _onSaved = MutableSharedFlow<CustomerEntity>(replay = 0)
    val onSaved: SharedFlow<CustomerEntity> = _onSaved.asSharedFlow()

    fun createCustomer(
        name: String,
        nickName: String?,
        userType: String,
        phone: String?,
        village: String,
        address: String? = null
    ) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                val base = _uiState.value.editingCustomer
                val entity = if (base != null) {
                    base.copy(
                        userName = name,
                        phone = phone ?: base.phone,
                        village = village,
                        nickName = nickName,
                        address = address ?: (nickName ?: base.address),
                        customerType = userType,
                    )
                } else {
                    CustomerEntity(
                        userName = name,
                        phone = phone ?: "",
                        village = village,
                        address = address ?: "",
                        nickName = nickName?:"",
                        customerType = userType,
                        sequenceNumber = 0,
                        createdDate = DateTimeUtil.currentUtcDateString(),
                        createdTime = DateTimeUtil.formatTo12HrTime(DateTimeUtil.currentUtcMillis()),
                        active = true
                    )
                }

                val withId = if (entity.userId == 0L) {
                    val newId = customerRepository.insertCustomer(entity)
                    entity.copy(userId = newId)
                } else {
                    customerRepository.updateCustomer(entity)
                    entity
                }
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = null)
                _onSaved.emit(withId)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = t.message)
            }
        }
    }

    fun startEditing(existing: CustomerEntity) {
        _uiState.value = _uiState.value.copy(editingCustomer = existing)
    }
}


