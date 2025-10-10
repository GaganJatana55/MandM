package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "money_transaction_edit_logs")
data class MoneyTransactionEditLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,                        // Unique log ID

    val moneyTransactionId: Long,             // FK -> MoneyTransactionEntity.id
    val updatedDateTimeStamp: String,         // When edit occurred (e.g. "2025-10-05 15:45:00")

    val userId: Long,
    val userName: String,

    val dateTimeStamp: String,                // Original transaction time
    val editedOn: String?,                    // Original transaction edit time
    val amount: Double,
    val transactionType: String,              // "Paid" or "Received"
    val note: String?                         // Optional description/remark
)


// --- 1️⃣ Convert from MoneyTransactionEntity → MoneyTransactionEditLogEntity ---
fun MoneyTransactionEntity.toEditLog(updatedDateTimeStamp: String): MoneyTransactionEditLogEntity {
    return MoneyTransactionEditLogEntity(
        moneyTransactionId = this.id,             // Link to main transaction
        updatedDateTimeStamp = updatedDateTimeStamp,
        userId = this.userId,
        userName = this.userName,
        dateTimeStamp = this.dateTimeStamp,
        editedOn = this.editedOn,
        amount = this.amount,
        transactionType = this.transactionType,

        note = this.note
    )
}

// --- 2️⃣ Convert from MoneyTransactionEditLogEntity → MoneyTransactionEntity ---
fun MoneyTransactionEditLogEntity.toMoneyTransaction(): MoneyTransactionEntity {
    return MoneyTransactionEntity(
        id = this.moneyTransactionId,
        userId = this.userId,
        userName = this.userName,
        dateTimeStamp = this.dateTimeStamp,
        editedOn = this.editedOn,
        amount = this.amount,
        transactionType = this.transactionType,
        note = this.note
    )
}
