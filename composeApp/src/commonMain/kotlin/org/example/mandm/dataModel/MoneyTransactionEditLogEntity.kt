package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
@Entity(
    tableName = "money_transaction_edit_logs",
    foreignKeys = [
        ForeignKey(
            entity = MoneyTransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["moneyTransactionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("moneyTransactionId")]
)
data class MoneyTransactionEditLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val moneyTransactionId: Long,
    val updatedDateTimeStamp: Long,  // ✅ Long

    val userId: Long,
    val userName: String,

    val dateTimeStamp: Long,
    val editedOn: Long?,

    val amount: Double,
    val transactionType: String,
    val note: String?
)

// --- 1️⃣ Convert from MoneyTransactionEntity → MoneyTransactionEditLogEntity ---
fun MoneyTransactionEntity.toEditLog(updatedDateTimeStamp: Long): MoneyTransactionEditLogEntity {
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
