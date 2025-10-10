package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milk_transactions")
data class MilkTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: Long,
    val userName: String,
    val dateTimeStamp: String,
    val editedOn: String? = null,
    val quantity: Double,
    val transactionType: String,
    val fixPrice: Boolean,
    val snfValue: Double,
    val snfPrice: Double,
    val fixPriceValue: Double,
    val PreFilledData: Boolean=false,
    val total: Double
)



// --- 1️⃣ Convert from MilkTransactionEntity → MilkTransactionEditLogEntity ---
fun MilkTransactionEntity.toEditLog(updatedDateTimeStamp: String): MilkTransactionEditLogEntity {
    return MilkTransactionEditLogEntity(
        milkTransactionId = this.id,             // Reference to main transaction
        updatedDateTimeStamp = updatedDateTimeStamp,

        userId = this.userId,
        userName = this.userName,

        dateTimeStamp = this.dateTimeStamp,
        editedOn = this.editedOn,
        quantity = this.quantity,
        transactionType = this.transactionType,
        fixPrice = this.fixPrice,
        snfValue = this.snfValue,
        snfPrice = this.snfPrice,
        fixPriceValue = this.fixPriceValue,
        PrefilledData = this.PreFilledData,
        total = this.total
    )
}

// --- 2️⃣ Convert from MilkTransactionEditLogEntity → MilkTransactionEntity ---
fun MilkTransactionEditLogEntity.toMilkTransaction(): MilkTransactionEntity {
    return MilkTransactionEntity(
        id = this.milkTransactionId,
        userId = this.userId,
        userName = this.userName,
        dateTimeStamp = this.dateTimeStamp,
        editedOn = this.editedOn,
        quantity = this.quantity,
        transactionType = this.transactionType,
        fixPrice = this.fixPrice,
        snfValue = this.snfValue,
        snfPrice = this.snfPrice,
        fixPriceValue = this.fixPriceValue,
        PreFilledData=this.PrefilledData,
        total = this.total
    )
}
