package org.example.mandm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.example.mandm.repo.BillRepository
import org.example.mandm.dataModel.BillEntity

data class BillingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val bills: List<BillEntity> = emptyList()
)

class BillingViewModel(
    private val billRepository: BillRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)
    private val _ui = MutableStateFlow(BillingUiState())
    val ui: StateFlow<BillingUiState> = _ui.asStateFlow()

    init {
        viewModelScope.launch {
            _userId.flatMapLatest { id ->
                if (id == null) kotlinx.coroutines.flow.flowOf(emptyList())
                else billRepository.getBillsForUser(id)
            }.collect { list ->
                _ui.value = _ui.value.copy(bills = list)
            }
        }
    }

    fun loadForUser(userId: Long) {
        _userId.value = userId
    }

    fun generateBill(userId: Long, userName: String, endDate: String, createdOn: String) {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, errorMessage = null)
            try {
                billRepository.generateBill(userId, userName, endDate, createdOn)
                _ui.value = _ui.value.copy(isLoading = false)
            } catch (t: Throwable) {
                _ui.value = _ui.value.copy(isLoading = false, errorMessage = t.message)
            }
        }
    }

    fun settleBill(bill: BillEntity, received: Double, paid: Double) {
        viewModelScope.launch {
            _ui.value = _ui.value.copy(isLoading = true, errorMessage = null)
            try {
                billRepository.settleBill(bill, amountReceived = received, amountPaid = paid)
                _ui.value = _ui.value.copy(isLoading = false)
            } catch (t: Throwable) {
                _ui.value = _ui.value.copy(isLoading = false, errorMessage = t.message)
            }
        }
    }

    fun buildShareText(bill: BillEntity): String {
        return buildString {
            appendLine("Bill for ${bill.userName}")
            appendLine("Range: ${bill.startDate} to ${bill.endDate}")
            appendLine("Milk: ${bill.totalMilkQuantity} L, Amount: ${bill.totalMilkAmount}")
            appendLine("Money: Received ${bill.moneyReceivedInRange}, Paid ${bill.moneyPaidInRange}")
            appendLine("Carry Forward: ${bill.carryForwardAmount}")
            appendLine("Settlements: Received ${bill.paymentsReceived}, Paid ${bill.paymentsPaid}")
            appendLine("Pending: ${bill.pendingAmount} | Status: ${bill.status}")
            append("Generated: ${bill.createdOn}")
        }
    }
}



