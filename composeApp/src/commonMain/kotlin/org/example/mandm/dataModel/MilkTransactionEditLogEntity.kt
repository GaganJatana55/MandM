package org.example.mandm.dataModel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milk_transaction_edit_logs")
data class MilkTransactionEditLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,                         // Unique log ID

    val milkTransactionId: Long,               // FK -> MilkTransactionEntity.id
    val updatedDateTimeStamp: String,          // When edit occurred (e.g. "2025-10-05 15:45:00")

    val userId: Long,
    val userName: String,

    val dateTimeStamp: String,                 // Original transaction time
    val editedOn: String?,                     // Original transaction edit time
    val quantity: Double,
    val transactionType: String,
    val fixPrice: Boolean,
    val snfValue: Double,
    val snfPrice: Double,
    val fixPriceValue: Double,
    val PrefilledData: Boolean,
    val total: Double
)
