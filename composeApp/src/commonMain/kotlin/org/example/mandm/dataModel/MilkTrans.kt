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


