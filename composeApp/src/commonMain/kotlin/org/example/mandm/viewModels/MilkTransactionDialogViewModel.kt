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
import org.example.mandm.dataModel.MoneyTransactionEntity
import org.example.mandm.repo.CustomerRepository
import org.example.mandm.repo.MilkRepository
import org.example.mandm.repo.MoneyRepository
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
    private val milkRepo: MilkRepository,
    private val moneyRepo: MoneyRepository
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

    fun initWith(
        routeMap: CustomerRouteEntity?,
        existingMilk: MilkTransactionEntity?,
        existingMoney: MoneyTransactionEntity?,
        initialCustomer: CustomerEntity?
    ) {
        viewModelScope.launch {
            val resolvedCustomer: CustomerEntity? = when {
                initialCustomer != null -> initialCustomer
                existingMilk?.userId != null -> customerRepo.getCustomerById(existingMilk.userId).firstOrNull()
                existingMoney?.userId != null -> customerRepo.getCustomerById(existingMoney.userId).firstOrNull()
                routeMap?.customerId != null -> customerRepo.getCustomerById(routeMap.customerId).firstOrNull()
                else -> null
            }
            _ui.value = if (resolvedCustomer != null) {
                _ui.value.copy(selectedCustomer = resolvedCustomer, query = resolvedCustomer.userName)
            } else {
                _ui.value.copy(selectedCustomer = null, query = "")
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

    suspend fun saveOrUpdateMoney(
        isEditing: Boolean,
        draft: MoneyTransactionEntity,
        timestamp: String
    ) {
        _ui.value = _ui.value.copy(isLoading = true, errorMessage = null)
        try {
            if (!isEditing) {
                moneyRepo.insertMoneyTransaction(draft)
            } else {
                moneyRepo.saveMoneyTransaction(draft, timestamp)
            }
            _ui.value = _ui.value.copy(isLoading = false)
        } catch (t: Throwable) {
            _ui.value = _ui.value.copy(isLoading = false, errorMessage = t.message)
        }
    }
}


