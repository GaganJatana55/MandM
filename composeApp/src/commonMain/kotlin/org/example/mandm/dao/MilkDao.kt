package org.example.mandm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import org.example.mandm.dataModel.MilkMonthlySummary
import org.example.mandm.dataModel.MilkTransactionEditLogEntity
import org.example.mandm.dataModel.MilkTransactionEntity
import org.example.mandm.dataModel.MilkTransactionWithLogs
import org.example.mandm.dataModel.toEditLog

@Dao
interface MilkDao {

    // MilkTransaction basic ops
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilkTransaction(transaction: MilkTransactionEntity): Long

    @Update
    suspend fun updateMilkTransaction(transaction: MilkTransactionEntity): Int

    @Delete
    suspend fun deleteMilkTransaction(transaction: MilkTransactionEntity): Int

    @Query("SELECT * FROM milk_transactions WHERE id = :id")
    fun getMilkTransactionById(id: Long): Flow<MilkTransactionEntity?>

    @Query("SELECT * FROM milk_transactions WHERE userId = :customerId ORDER BY dateTimeStamp DESC")
    fun getMilkTransactionsForCustomer(customerId: Long): Flow<List<MilkTransactionEntity>>

    @Query("SELECT * FROM milk_transactions WHERE userId = :customerId ORDER BY dateTimeStamp DESC LIMIT 1")
    fun getLatestMilkTransactionForCustomer(customerId: Long): Flow<MilkTransactionEntity?>

    @Query("SELECT * FROM milk_transactions ORDER BY dateTimeStamp DESC")
    fun getAllMilkTransactions(): Flow<List<MilkTransactionEntity>>

    // Logs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMilkEditLog(log: MilkTransactionEditLogEntity): Long

    @Query("SELECT * FROM milk_transaction_edit_logs WHERE milkTransactionId = :milkTransactionId ORDER BY updatedDateTimeStamp DESC")
    fun getMilkEditLogsForTransaction(milkTransactionId: Long): Flow<List<MilkTransactionEditLogEntity>>

    @Query("SELECT * FROM milk_transaction_edit_logs WHERE userId = :customerId ORDER BY updatedDateTimeStamp DESC")
    fun getMilkEditLogsForCustomer(customerId: Long): Flow<List<MilkTransactionEditLogEntity>>

    // Transactional update with log
    @Transaction
    suspend fun updateMilkTransactionWithLog(updated: MilkTransactionEntity, updatedTime: Long) {
        val existing = getMilkTransactionById(updated.id).firstOrNull()
        if (existing != null) {
            insertMilkEditLog(existing.toEditLog(updatedTime))
        }
        updateMilkTransaction(updated)
    }

    @Transaction
    @Query("""
    SELECT * FROM milk_transactions
    WHERE userId = :userId
    ORDER BY dateTimeStamp DESC
""")
    fun getMilkTransactionsWithLogs(
        userId: Long
    ): Flow<List<MilkTransactionWithLogs>>


    @Transaction
    @Query("""
    SELECT * FROM milk_transactions
    WHERE userId = :userId
    AND dateTimeStamp BETWEEN :start AND :end
    ORDER BY dateTimeStamp DESC
""")
    fun getMilkTransactionsWithLogsByRange(
        userId: Long,
        start: Long,
        end: Long
    ): Flow<List<MilkTransactionWithLogs>>

    @Query("""
    SELECT 
        COALESCE(SUM(CASE 
            WHEN transactionType = 'Sold' 
            THEN quantity ELSE 0 END), 0) as milkSoldQty,

        COALESCE(SUM(CASE 
            WHEN transactionType = 'Sold' 
            THEN total ELSE 0 END), 0) as milkSoldAmount,

        COALESCE(SUM(CASE 
            WHEN transactionType = 'Buy' 
            THEN quantity ELSE 0 END), 0) as milkBoughtQty,

        COALESCE(SUM(CASE 
            WHEN transactionType = 'Buy' 
            THEN total ELSE 0 END), 0) as milkBoughtAmount

    FROM milk_transactions
    WHERE userId = :userId
    AND dateTimeStamp BETWEEN :start AND :end
""")
    fun getMilkMonthlySummary(
        userId: Long,
        start: Long,
        end: Long
    ): Flow<MilkMonthlySummary>
}


