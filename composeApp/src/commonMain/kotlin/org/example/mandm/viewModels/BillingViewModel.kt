package org.example.mandm.viewModels

import YearMonth
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.mandm.DateTimeUtil
import org.example.mandm.dataModel.BillEntity
import org.example.mandm.dataModel.LedgerItem
import org.example.mandm.dataModel.MonthlyLedgerSummary
import org.example.mandm.repo.BillRepository
import org.example.mandm.repo.MilkRepository
import org.example.mandm.repo.MoneyRepository

data class BillingUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val bills: List<BillEntity> = emptyList()
)

class BillingViewModel(
    private val billRepository: BillRepository,
    private val milkRepository: MilkRepository,
    private val moneyRepository: MoneyRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<Long?>(null)
    private val _ui = MutableStateFlow(BillingUiState())
    val ui: StateFlow<BillingUiState> = _ui.asStateFlow()

     val selectedYearMonth =
        MutableStateFlow(DateTimeUtil.getCurrentYearMonth())

    fun setYearMonth(yearMonth: YearMonth) {
        selectedYearMonth.value = yearMonth
    }

    val totalAmountForMonth = MutableStateFlow<Int>(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val ledger: StateFlow<List<LedgerItem>> =
        combine(
            _userId,
            selectedYearMonth
        ) { userId, yearMonth ->
            userId to yearMonth
        }
            .filter { it.first != null }
            .flatMapLatest { (userId, yearMonth) ->

                val start = yearMonth.startOfMonthMillis()
                val end = yearMonth.endOfMonthMillis()

                combine(
                    milkRepository.getMilkTransactionsByRange(userId!!, start, end),
                    moneyRepository.getMoneyTransactionsByRange(userId, start, end)
                ) { milkList, moneyList ->

                    val items =
                        milkList.map {

                            LedgerItem.Milk(it)
                        } +
                                moneyList.map { LedgerItem.Money(it) }

                    items.sortedByDescending { it.dateTime }
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val monthlySummary: StateFlow<MonthlyLedgerSummary> =
        combine(
            _userId,
            selectedYearMonth
        ) { userId, yearMonth ->
            userId to yearMonth
        }
            .filter { it.first != null }
            .flatMapLatest { (userId, yearMonth) ->

                val start = yearMonth.startOfMonthMillis()
                val end = yearMonth.endOfMonthMillis()

                combine(
                    milkRepository.getMilkMonthSummary(userId!!, start, end),
                    moneyRepository.getMoneyMonthSummary(userId, start, end)
                ) { milk, money ->

                    MonthlyLedgerSummary(
                        milk = milk,
                        money = money,
                        openingBalance = 0.0 // adjust later if you add carry-forward
                    )
                }
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                MonthlyLedgerSummary()
            )

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


}




