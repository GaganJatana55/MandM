package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Bill aggregates milk and money transactions over a date range for a single user.
 * Pending amount is derived as: carryForward + totalMilkAmount - netMoneyTransactions - paymentsReceived + paymentsPaid
 * where netMoneyTransactions = moneyReceived - moneyPaid from MoneyTransactionEntity within the bill range.
 */
@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val userId: Long,
    val userName: String,

    // Inclusive range in yyyy-MM-dd
    val startDate: String,
    val endDate: String,

    // Milk summary
    val totalMilkQuantity: Double,
    val totalMilkAmount: Double,

    // Money transactions that occurred in the same range
    val moneyReceivedInRange: Double, // Money RECEIVED
    val moneyPaidInRange: Double,     // Money PAID

    // Carry forward from previous bill's pending
    val carryForwardAmount: Double,

    // Manual settlements applied to this bill after generation
    val paymentsReceived: Double = 0.0,
    val paymentsPaid: Double = 0.0,

    // Computed pending at the time of last update
    val pendingAmount: Double,

    // "Pending", "Partial", or "Paid"
    val status: String,

    // Creation timestamp string for display (e.g. "2025-10-12 14:32:00")
    val createdOn: String
)



