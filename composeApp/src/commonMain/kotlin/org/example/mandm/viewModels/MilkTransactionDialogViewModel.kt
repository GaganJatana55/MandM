package org.example.mandm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.example.mandm.dataModel.CustomerEntity
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.repo.CustomerRepository
import org.example.mandm.repo.MilkRepository
import kotlinx.coroutines.flow.firstOrNull
import org.example.mandm.dataModel.CustomerRouteEntity

data class MilkTxUiState(
    val query: String = "",
    val searchResults: List<CustomerEntity> = emptyList(),
    val selectedCustomer: CustomerEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MilkTransactionDialogViewModel(
    private val customerRepo: CustomerRepository,
    private val milkRepo: MilkRepository
) : ViewModel() {
    private val _ui = MutableStateFlow(MilkTxUiState())
    val ui: StateFlow<MilkTxUiState> = _ui.asStateFlow()

    init {
        observeSearch()
    }

    private fun observeSearch() {
        viewModelScope.launch {
            _ui
                .map { it.query }
                .debounce(200)
                .distinctUntilChanged()
                .flatMapLatest { q ->
                    if (q.isBlank()) customerRepo.getAllCustomers() else customerRepo.searchCustomersByNameOrPhone("%$q%")
                }
                .collect { list ->
                    _ui.value = _ui.value.copy(searchResults = list)
                }
        }
    }

    fun updateQuery(q: String) {
        _ui.value = _ui.value.copy(query = q)
    }

    fun selectCustomer(c: CustomerEntity) {
        _ui.value = _ui.value.copy(selectedCustomer = c, query = c.userName)
    }

    fun initWith(routeMap: CustomerRouteEntity?, existing: MilkTransactionEntity?) {
        viewModelScope.launch {
            val idFromRoute = routeMap?.customerId
            val idFromTx = existing?.userId
            val id = idFromRoute ?: idFromTx
            if (id != null) {
                val customer = customerRepo.getCustomerById(id).firstOrNull()
                if (customer != null) {
                    _ui.value = _ui.value.copy(selectedCustomer = customer, query = customer.userName)
                }
            } else {
                // New dialog without defaults: clear previous selection and query
                _ui.value = _ui.value.copy(selectedCustomer = null, query = "")
            }
        }
    }

    suspend fun saveOrUpdate(
        isEditing: Boolean,
        draft: MilkTransactionEntity,
        timestamp: String
    ) {
        _ui.value = _ui.value.copy(isLoading = true, errorMessage = null)
        try {
            if (!isEditing) {
                milkRepo.insertMilkTransaction(draft)
            } else {
                milkRepo.saveMilkTransaction(draft, timestamp)
            }
            _ui.value = _ui.value.copy(isLoading = false)
        } catch (t: Throwable) {
            _ui.value = _ui.value.copy(isLoading = false, errorMessage = t.message)
        }
    }
}


