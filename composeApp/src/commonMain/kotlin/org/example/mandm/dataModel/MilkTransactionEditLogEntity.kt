package org.example.mandm.dataModel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "milk_transaction_edit_logs",
    foreignKeys = [
        ForeignKey(
            entity = MilkTransactionEntity::class,
            parentColumns = ["id"],
            childColumns = ["milkTransactionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("milkTransactionId")]
)
data class MilkTransactionEditLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val milkTransactionId: Long,
    val updatedDateTimeStamp: Long,

    val userId: Long,
    val userName: String,

    val dateTimeStamp: Long,
    val editedOn: String?,
    val quantity: Double,
    val transactionType: String,
    val fixPrice: Boolean,
    val snfValue: Double,
    val snfPrice: Double,
    val fixPriceValue: Double,
    val PrefilledData: Boolean,
    val total: Double
)

data class MilkTransactionWithLogs(

    @Embedded
    val transaction: MilkTransactionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "milkTransactionId"
    )
    val editLogs: List<MilkTransactionEditLogEntity>
)