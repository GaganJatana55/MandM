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
import org.example.mandm.dataModel.MoneyMonthlySummary
import org.example.mandm.dataModel.MoneyTransactionEditLogEntity
import org.example.mandm.dataModel.MoneyTransactionEntity
import org.example.mandm.dataModel.MoneyTransactionWithLogs
import org.example.mandm.dataModel.toEditLog

@Dao
interface MoneyDao {

    // MoneyTransaction basic ops
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneyTransaction(transaction: MoneyTransactionEntity): Long

    @Update
    suspend fun updateMoneyTransaction(transaction: MoneyTransactionEntity): Int

    @Delete
    suspend fun deleteMoneyTransaction(transaction: MoneyTransactionEntity): Int

    @Query("SELECT * FROM money_transactions WHERE id = :id")
    fun getMoneyTransactionById(id: Long): Flow<MoneyTransactionEntity?>

    @Query("SELECT * FROM money_transactions WHERE userId = :customerId ORDER BY dateTimeStamp DESC")
    fun getMoneyTransactionsForCustomer(customerId: Long): Flow<List<MoneyTransactionEntity>>

    @Query("SELECT * FROM money_transactions WHERE userId = :customerId ORDER BY dateTimeStamp DESC LIMIT 1")
    fun getLatestMoneyTransactionForCustomer(customerId: Long): Flow<MoneyTransactionEntity?>

    @Query("SELECT * FROM money_transactions ORDER BY dateTimeStamp DESC")
    fun getAllMoneyTransactions(): Flow<List<MoneyTransactionEntity>>

    // Logs
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoneyEditLog(log: MoneyTransactionEditLogEntity): Long

    @Query("SELECT * FROM money_transaction_edit_logs WHERE moneyTransactionId = :moneyTransactionId ORDER BY updatedDateTimeStamp DESC")
    fun getMoneyEditLogsForTransaction(moneyTransactionId: Long): Flow<List<MoneyTransactionEditLogEntity>>

    @Query("SELECT * FROM money_transaction_edit_logs WHERE userId = :customerId ORDER BY updatedDateTimeStamp DESC")
    fun getMoneyEditLogsForCustomer(customerId: Long): Flow<List<MoneyTransactionEditLogEntity>>

    // Transactional update with log
    @Transaction
    suspend fun updateMoneyTransactionWithLog(updated: MoneyTransactionEntity, updatedTime: Long) {
        val existing = getMoneyTransactionById(updated.id).firstOrNull()
        if (existing != null) {
            insertMoneyEditLog(existing.toEditLog(updatedTime))
        }
        updateMoneyTransaction(updated)
    }

    @Transaction
    @Query("""
    SELECT * FROM money_transactions
    WHERE userId = :userId
    ORDER BY dateTimeStamp DESC
""")
    fun getMoneyTransactionsWithLogs(
        userId: Long
    ): Flow<List<MoneyTransactionWithLogs>>

    @Transaction
    @Query("""
    SELECT * FROM money_transactions
    WHERE userId = :userId
    AND dateTimeStamp BETWEEN :start AND :end
    ORDER BY dateTimeStamp DESC
""")
    fun getMoneyTransactionsWithLogsByRange(
        userId: Long,
        start: Long,
        end: Long
    ): Flow<List<MoneyTransactionWithLogs>>
    @Query("""
    SELECT 
        COALESCE(SUM(CASE 
            WHEN transactionType = 'Received' 
            THEN amount ELSE 0 END), 0) as moneyReceived,

        COALESCE(SUM(CASE 
            WHEN transactionType = 'Paid' 
            THEN amount ELSE 0 END), 0) as moneyPaid

    FROM money_transactions
    WHERE userId = :userId
    AND dateTimeStamp BETWEEN :start AND :end
""")
    fun getMoneyMonthlySummary(
        userId: Long,
        start: Long,
        end: Long
    ): Flow<MoneyMonthlySummary>
}



