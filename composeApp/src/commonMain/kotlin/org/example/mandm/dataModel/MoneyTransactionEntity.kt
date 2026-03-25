package org.example.mandm.dataModel

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "money_transactions",
    indices = [Index("userId")]
)
data class MoneyTransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val userId: Long,
    val userName: String,

    val dateTimeStamp: Long,      // ✅ Use Long
    val editedOn: Long? = null,   // ✅ Use Long

    val amount: Double,
    val transactionType: String,  // Later can convert to enum
    val note: String? = null
)

data class MoneyTransactionWithLogs(

    @Embedded
    val transaction: MoneyTransactionEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "moneyTransactionId"
    )
    val editLogs: List<MoneyTransactionEditLogEntity>
)