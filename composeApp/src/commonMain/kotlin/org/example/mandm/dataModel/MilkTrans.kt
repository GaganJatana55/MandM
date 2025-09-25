package org.example.mandm.dataModel

import androidx.room.PrimaryKey
import org.example.mandm.DateTimeUtil
import org.example.mandm.TransactionStatus
import org.example.mandm.TransactionTypeConstants


data class MilkTrans(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var type: String = TransactionTypeConstants.Milk.SELL,
    var userId: Int = -1,
    var userName: String = "UNKNOWN",
    var fixPrice: Boolean = true,
    var SNF: Double = -1.0,
    var SNFPrice: Double = 5.0,
    var quantity: Double = 0.0,
    var price: Double = 50.0,
    var date: String = DateTimeUtil.currentUtcDateString(), // "2025-08-01"
    var time: Long = DateTimeUtil.currentUtcMillis(), // epoch millis in UTC
    var status: String = TransactionStatus.PENDING
)
val statuses = listOf(
    TransactionStatus.PENDING,
    TransactionStatus.SKIPPED,
    TransactionStatus.ADDED
)

fun randomStatus() = statuses.random()

val sampleTransactions = listOf(
    MilkTrans(userId = 1, userName = "Ramesh", type = TransactionTypeConstants.Milk.BUY,  quantity = 5.2,   status = randomStatus()),
    MilkTrans(userId = 2, userName = "Sunita", type = TransactionTypeConstants.Milk.BUY,  quantity = 7.0,   fixPrice = false, SNF = 7.2, SNFPrice = 10.0, status = randomStatus()),
    MilkTrans(userId = 3, userName = "Sharma", type = TransactionTypeConstants.Milk.SELL, quantity = 4.75,  price = 60.0, status = randomStatus()),
    MilkTrans(userId = 4, userName = "Kiran",  type = TransactionTypeConstants.Milk.SELL, quantity = 6.3,   price = 62.0, status = randomStatus()),
    MilkTrans(userId = 5, userName = "Ravi",   type = TransactionTypeConstants.Milk.BUY,  quantity = 8.1,   status = randomStatus()),
    MilkTrans(userId = 6, userName = "Neha",   type = TransactionTypeConstants.Milk.BUY,  quantity = 6.4,   fixPrice = false, SNF = 8.0, SNFPrice = 9.0, status = randomStatus()),
    MilkTrans(userId = 7, userName = "Ajay",   type = TransactionTypeConstants.Milk.SELL, quantity = 3.5,   price = 55.0, status = randomStatus()),
    MilkTrans(userId = 8, userName = "Deepa",  type = TransactionTypeConstants.Milk.SELL, quantity = 7.75,  price = 58.0, status = randomStatus()),
    MilkTrans(userId = 9, userName = "Bharat", type = TransactionTypeConstants.Milk.BUY,  quantity = 9.25,  fixPrice = false, SNF = 6.5, SNFPrice = 11.0, status = randomStatus()),
    MilkTrans(userId = 10,userName = "Manju",  type = TransactionTypeConstants.Milk.BUY,  quantity = 4.6,   status = randomStatus()),
    MilkTrans(userId = 11,userName = "Kanta",  type = TransactionTypeConstants.Milk.SELL, quantity = 6.05,  price = 59.0, status = randomStatus()),
    MilkTrans(userId = 12,userName = "Rahul",  type = TransactionTypeConstants.Milk.BUY,  quantity = 5.4,   status = randomStatus()),
    MilkTrans(userId = 13,userName = "Aarti",  type = TransactionTypeConstants.Milk.BUY,  quantity = 7.2,   fixPrice = false, SNF = 6.8, SNFPrice = 9.5, status = randomStatus()),
    MilkTrans(userId = 14,userName = "Gopal",  type = TransactionTypeConstants.Milk.SELL, quantity = 3.2,   price = 56.0, status = randomStatus()),
    MilkTrans(userId = 15,userName = "Ritu",   type = TransactionTypeConstants.Milk.SELL, quantity = 6.85,  price = 61.0, status = randomStatus())
)
fun sortTransactionsByStatus(transactions: List<MilkTrans>): List<MilkTrans> {
    val priority = mapOf(
        TransactionStatus.ADDED to 0,
        TransactionStatus.SKIPPED to 1,
        TransactionStatus.PENDING to 2
    )
    return transactions.sortedBy { priority[it.status] ?: Int.MAX_VALUE }
}
