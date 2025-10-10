package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "money_transactions")
data class MoneyTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,                  // Unique transaction ID

    val userId: Long,                   // FK -> UserEntity.userId
    val userName: String,               // For quick reference/display

    val dateTimeStamp: String,          // When the transaction occurred (e.g. "2025-10-05 10:15:30")
    val editedOn: String? = null,       // When it was last modified (nullable)

    val amount: Double,                 // Amount of money involved
    val transactionType: String,        // "Paid" or "Received"
    val note: String? = null            // Optional: add remarks or reason
)
