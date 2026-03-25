package org.example.mandm.dataModel

data class MonthlyLedgerSummary(
    val milk: MilkMonthlySummary = MilkMonthlySummary(),
    val money: MoneyMonthlySummary = MoneyMonthlySummary(),
    val openingBalance: Double = 0.0
) {

    val monthNetBalance: Double
        get() =
            milk.milkSoldAmount- milk.milkBoughtAmount- money.moneyReceived+ money.moneyPaid

    val closingBalance: Double
        get() = openingBalance + monthNetBalance

    val balanceUiState: BalanceUiState
        get() = when {
            closingBalance > 0 -> BalanceUiState.YOU_GET
            closingBalance < 0 -> BalanceUiState.YOU_PAY
            else -> BalanceUiState.SETTLED
        }

    val displayAmount: Double
        get() = kotlin.math.abs(closingBalance)

}

enum class BalanceUiState {
    YOU_GET,      // Green
    YOU_PAY,      // Red
    SETTLED       // Neutral
}

data class MoneyMonthlySummary(
    val moneyReceived: Double = 0.0,
    val moneyPaid: Double = 0.0
) {
    val netMoneyAmount: Double
        get() = moneyPaid - moneyReceived
}
data class MilkMonthlySummary(
    val milkSoldQty: Double = 0.0,
    val milkSoldAmount: Double = 0.0,
    val milkBoughtQty: Double = 0.0,
    val milkBoughtAmount: Double = 0.0
) {
    val netMilkAmount: Double
        get() = milkSoldAmount - milkBoughtAmount

    val netMilkQty: Double
        get() = milkSoldQty - milkBoughtQty
}